package org.muellners.finscale.identity.config

import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.env.Environment

@Configuration
@Profile("dev")
class MainDataSourceConfiguration(val env: Environment) {

    @Bean
    @Qualifier("mainDataSource")
    fun mainDataSource(): DataSource = HikariDataSource()
        .apply {
            jdbcUrl = env.getProperty("spring.datasource.url")
            username = env.getProperty("spring.datasource.username")
            password = env.getProperty("spring.datasource.password")
    }
}
