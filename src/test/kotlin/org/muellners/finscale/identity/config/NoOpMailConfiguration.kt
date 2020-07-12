package org.muellners.finscale.identity.config

import org.mockito.Mockito
import org.muellners.finscale.identity.domain.User
import org.muellners.finscale.identity.service.MailService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class NoOpMailConfiguration {
    private val mockMailService = Mockito.mock(MailService::class.java)

    @Bean
    fun mailService(): MailService {
        return mockMailService
    }

    init {
        Mockito.doNothing().`when`(mockMailService).sendActivationEmail(User())
    }
}
