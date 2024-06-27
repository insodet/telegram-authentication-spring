package com.vk.romindx.telegrambotsSpringWrapper.authentication.validation

import com.vk.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication

internal abstract class TelegramAuthenticator {

    abstract fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean

    fun authenticate(authentication: TelegramAuthentication, botToken: String): TelegramAuthentication =
        authentication.also {
            if (isValid(it, botToken)) {
                it.isAuthenticated = true
            } else {
                throw AuthenticationError.ValidationError("Invalid authentication object")
            }
        }
}