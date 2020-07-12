package org.muellners.finscale.identity.service

import com.zaxxer.hikari.HikariDataSource
import java.lang.Exception
import javax.sql.DataSource
import liquibase.integration.spring.SpringLiquibase
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.data.redis.stream.StreamListener
import org.springframework.stereotype.Service

@Service
class TenantConsumerService(
    private val redisTemplate: StringRedisTemplate,
    private val springLiquibase: SpringLiquibase,
    val tenantDataSourceMap: MutableMap<Any, DataSource>
) : StreamListener<String, MapRecord<String, String, String>> {

    override fun onMessage(message: MapRecord<String, String, String>) {
        val tenantIdentifier = message.value["identifier"]
        val tenantDataSource: DataSource = HikariDataSource()
            .apply {
                jdbcUrl = "jdbc:h2:file:./build/h2db/db/$tenantIdentifier;DB_CLOSE_DELAY=-1"
                username = "identity"
                password = ""
            }

        // To be removed when automatic deserialization comes in
        if (tenantIdentifier == null) {
            throw Exception("Tenant identifier cannot be blank.")
        }

        tenantDataSourceMap.putIfAbsent(tenantIdentifier, tenantDataSource)

        springLiquibase.setShouldRun(true)
        springLiquibase.dataSource = tenantDataSource
        springLiquibase.afterPropertiesSet()

        println("New tenant created!!!")
        println(message.value)
    }
}
