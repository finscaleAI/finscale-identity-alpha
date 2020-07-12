package org.muellners.finscale.identity.config

import io.lettuce.core.protocol.CommandKeyword
import io.lettuce.core.protocol.CommandType
import java.net.InetAddress
import java.time.Duration
import javax.annotation.PostConstruct
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.stream.*
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.stream.StreamListener
import org.springframework.data.redis.stream.StreamMessageListenerContainer
import org.springframework.data.redis.stream.Subscription

@Configuration
class RedisStreamConfiguration(
//    private val streamListener: StreamListener<String, ObjectRecord<String, TenantDTO>>,
    private val streamListener: StreamListener<String, MapRecord<String, String, String>>,
    private val redisTemplate: StringRedisTemplate
) {
    val streamName = "core:tenants" // Should be made a constant provided by the finscale-multitenancy lib project
    val consumerGroupName = "identity-service" // Should be made some kind of project constant

    @PostConstruct
    fun createTenantConsumerGroup() {
        try {
            redisTemplate.execute(
                RedisCallback { connection: RedisConnection ->
                    val result = connection.execute(CommandType.XGROUP.name,
                        CommandKeyword.CREATE.name.toByteArray(), streamName.toByteArray(),
                        consumerGroupName.toByteArray(), ReadOffset.latest().offset.toByteArray(),
                        "MKSTREAM".toByteArray())

                    result
                } as RedisCallback<Any>)
        } catch (e: Exception) {
        }
    }

    @Bean
    fun tenantSubscription(redisConnectionFactory: RedisConnectionFactory): Subscription {
        val options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
            .builder()
            .pollTimeout(Duration.ofSeconds(5))
//            .targetType(TenantDTO::class.java)
            .build()

        val listenerContainer = StreamMessageListenerContainer
            .create(redisConnectionFactory, options)
        val subscription = listenerContainer.receiveAutoAck(
            Consumer.from(consumerGroupName, InetAddress.getLocalHost().hostName),
            StreamOffset.create(streamName, ReadOffset.lastConsumed()),
            this.streamListener)

        listenerContainer.start()
        return subscription
    }
}
