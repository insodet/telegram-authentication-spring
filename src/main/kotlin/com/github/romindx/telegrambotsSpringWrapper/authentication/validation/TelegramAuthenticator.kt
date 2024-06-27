package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

import com.github.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication

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