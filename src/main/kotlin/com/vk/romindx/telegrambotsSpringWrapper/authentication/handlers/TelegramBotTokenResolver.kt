package com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers

import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication

interface TelegramBotTokenResolver {
    fun resolve(authentication: TelegramAuthentication): String
}