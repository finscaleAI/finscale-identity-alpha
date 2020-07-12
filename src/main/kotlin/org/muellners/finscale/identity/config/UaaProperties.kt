package org.muellners.finscale.identity.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Properties for UAA-based OAuth2 security.
 */
@Component
@ConfigurationProperties(prefix = "uaa", ignoreUnknownFields = false)
class UaaProperties {
    val keyStore = KeyStore()

    val webClientConfiguration = WebClientConfiguration()

    /**
     * Keystore configuration for signing and verifying JWT tokens.
     */
    class KeyStore {
        // name of the keystore in the classpath
        var name = "config/tls/keystore.p12"
        // password used to access the key
        var password = "password"
        // name of the alias to fetch
        var alias = "selfsigned"
    }

    class WebClientConfiguration {
        // validity of the short-lived access token in secs (min: 60), don't make it too long
        var accessTokenValidityInSeconds = 5 * 60
        // validity of the refresh token in secs (defines the duration of "remember me")
        var refreshTokenValidityInSecondsForRememberMe = 7 * 24 * 60 * 60
        var clientId = "web_app"
        var secret = "changeit"
    }
}
