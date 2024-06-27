package com.github.romindx.telegrambotsSpringWrapper.authentication.handlers

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication

interface TelegramBotTokenResolver {
    fun resolve(authentication: TelegramAuthentication): String
}