package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.hmacSha256
import com.github.romindx.telegrambotsSpringWrapper.hmacSha256String

internal class MiniAppValidation: TelegramAuthenticator() {
    override fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean =
        authentication
            .checkString
            .hmacSha256String(botToken.hmacSha256("WebAppData".toByteArray())) == authentication.credentials
}