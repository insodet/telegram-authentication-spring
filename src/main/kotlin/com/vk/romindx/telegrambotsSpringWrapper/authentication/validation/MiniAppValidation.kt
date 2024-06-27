package com.vk.romindx.telegrambotsSpringWrapper.authentication.validation

import com.vk.romindx.telegrambotsSpringWrapper.*
import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.vk.romindx.telegrambotsSpringWrapper.getCheckAttributes
import com.vk.romindx.telegrambotsSpringWrapper.hmacSha256
import com.vk.romindx.telegrambotsSpringWrapper.toCheckString

internal class MiniAppValidation: TelegramAuthenticator() {
    override fun isValid(authentication: TelegramAuthentication, botToken: String): Boolean =
        authentication
            .principal
            .getCheckAttributes()
            .toCheckString()
            .hmacSha256String(botToken.hmacSha256("WebAppData".toByteArray())) == authentication.principal.hash
}