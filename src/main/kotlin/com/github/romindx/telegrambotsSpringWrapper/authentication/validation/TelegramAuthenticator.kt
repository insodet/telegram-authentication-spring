package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

import com.github.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import java.util.*

internal abstract class TelegramAuthenticator {
    abstract fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean

    fun authenticate(authentication: TelegramAuthentication, botToken: String): TelegramAuthentication =
        if (authentication.authDate.let { (it?.time ?: 0) > Date().time - 86400000 } && isValid(authentication, botToken)) {
            authentication.isAuthenticated = true
            authentication
        } else {
            throw AuthenticationError.ValidationError("Invalid authentication object")
        }
}