package org.muellners.finscale.identity.config

import javax.annotation.PostConstruct
import org.springframework.beans.factory.BeanInitializationException
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension

@Configuration
class UaaWebSecurityConfiguration(
    private val userDetailsService: UserDetailsService,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) : WebSecurityConfigurerAdapter() {

    @PostConstruct
    @Throws(Exception::class)
    fun init() {
        try {
            authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
        } catch (e: Exception) {
            throw BeanInitializationException("Security configuration failed", e)
        }
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Throws(Exception::class)
    override fun configure(web: WebSecurity?) {
        web!!.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**")
            .antMatchers("/h2-console/**")
    }

    @Bean
    fun securityEvaluationContextExtension() = SecurityEvaluationContextExtension()
}
