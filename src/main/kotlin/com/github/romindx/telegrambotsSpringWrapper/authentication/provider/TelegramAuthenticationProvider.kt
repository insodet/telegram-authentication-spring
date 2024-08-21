package com.github.romindx.telegrambotsSpringWrapper.authentication.provider

import com.github.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

internal class TelegramAuthenticationProvider(
    private val tokenResolver: TelegramBotTokenResolver?,
    private val successHandler: SuccessValidationHandler?
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication? =
        (authentication as? TelegramAuthentication)
            ?.authenticate(tokenResolver)
            ?.let { validAuthentication ->
                successHandler
                    ?.let { it.onSuccessValidation(validAuthentication) ?: throw AuthenticationError.ValidationError("Authentication not permitted") } ?: validAuthentication
            }

    override fun supports(authentication: Class<*>?): Boolean =
        authentication?.let { TelegramAuthentication::class.java.isAssignableFrom(it) } ?: false
}

private fun TelegramAuthentication.authenticate(tokenResolver: TelegramBotTokenResolver?) =
    details
        .getAuthenticator()
        .authenticate(
            this,
            tokenResolver?.resolve(this) ?: throw AuthenticationError.ValidationError("Resolved token is null")
        )