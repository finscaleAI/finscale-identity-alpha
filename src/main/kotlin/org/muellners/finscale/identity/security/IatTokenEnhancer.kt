package org.muellners.finscale.identity.security

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken
import org.springframework.security.oauth2.common.OAuth2AccessToken
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.token.TokenEnhancer
import org.springframework.stereotype.Component

/**
 * Adds the standard "iat" claim to tokens so we know when they have been created.
 * This is needed for a session timeout due to inactivity (ignored in case of "remember-me").
 */
@Component
class IatTokenEnhancer : TokenEnhancer {

    override fun enhance(accessToken: OAuth2AccessToken, authentication: OAuth2Authentication): OAuth2AccessToken {
        addClaims(accessToken as DefaultOAuth2AccessToken)
        return accessToken
    }

    private fun addClaims(accessToken: DefaultOAuth2AccessToken) {
        var additionalInformation: MutableMap<String, Any> = accessToken.additionalInformation
        if (additionalInformation.isEmpty()) {
            additionalInformation = mutableMapOf()
        }
        // add "iat" claim with current time in secs
        // this is used for an inactive session timeout
        additionalInformation["iat"] = (System.currentTimeMillis() / 1000L).toInt()
        accessToken.additionalInformation = additionalInformation
    }
}
