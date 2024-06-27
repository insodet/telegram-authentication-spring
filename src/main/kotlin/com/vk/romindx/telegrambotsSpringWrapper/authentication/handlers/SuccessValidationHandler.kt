package com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers

import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import org.springframework.security.core.Authentication

interface SuccessValidationHandler {
    fun onSuccessValidation(authentication: TelegramAuthentication): Authentication
}