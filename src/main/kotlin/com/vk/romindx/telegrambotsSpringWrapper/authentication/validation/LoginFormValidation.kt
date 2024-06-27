package com.vk.romindx.telegrambotsSpringWrapper.authentication.validation

import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.vk.romindx.telegrambotsSpringWrapper.getCheckAttributes
import com.vk.romindx.telegrambotsSpringWrapper.hmacSha256String
import com.vk.romindx.telegrambotsSpringWrapper.sha256hash
import com.vk.romindx.telegrambotsSpringWrapper.toCheckString

internal class LoginFormValidation: TelegramAuthenticator() {
    override fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean =
        authentication
            .principal
            .getCheckAttributes()
            .toCheckString()
            .hmacSha256String(botToken.sha256hash) == authentication.principal.hash
}