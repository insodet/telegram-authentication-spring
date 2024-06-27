package com.vk.romindx.telegrambotsSpringWrapper.authentication.provider

import com.vk.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication

internal class TelegramAuthenticationProvider(
    private val tokenResolver: TelegramBotTokenResolver?,
    private val successHandler: SuccessValidationHandler?
): AuthenticationProvider {

    override fun authenticate(authentication: Authentication?): Authentication? =
        (authentication as? TelegramAuthentication)
            ?.authenticate(tokenResolver)
            ?.let { successHandler?.onSuccessValidation(it) ?: it }

    override fun supports(authentication: Class<*>?): Boolean =
        authentication?.let { TelegramAuthentication::class.java.isAssignableFrom(it) } ?: false
}

private fun TelegramAuthentication.authenticate(tokenResolver: TelegramBotTokenResolver?) =
    userType
        .getAuthenticator()
        .authenticate(
            this,
            tokenResolver?.resolve(this) ?: throw AuthenticationError.ValidationError("Resolved token is null")
        )