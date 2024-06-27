package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.getCheckAttributes
import com.github.romindx.telegrambotsSpringWrapper.hmacSha256String
import com.github.romindx.telegrambotsSpringWrapper.sha256hash
import com.github.romindx.telegrambotsSpringWrapper.toCheckString

internal class LoginFormValidation: TelegramAuthenticator() {
    override fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean =
        authentication
            .principal
            .getCheckAttributes()
            .toCheckString()
            .hmacSha256String(botToken.sha256hash) == authentication.principal.hash
}