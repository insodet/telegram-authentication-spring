package com.github.romindx.telegrambotsSpringWrapper.authentication.handlers

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import org.springframework.security.core.Authentication

interface SuccessValidationHandler {
    fun onSuccessValidation(authentication: TelegramAuthentication): Authentication
}