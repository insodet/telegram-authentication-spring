@file:Suppress("unused")
package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.core.Authentication

class TelegramFilterBuilder<H : HttpSecurityBuilder<H>?>(private val builder: TelegramAuthenticationBuilder<H>, private val configurer: TelegramFilterConfigurer) {

    fun tokenResolver(resolver: TelegramBotTokenResolver) = this.also {
        configurer.tokenResolver(resolver)
    }

    fun tokenResolver(resolveHandler: (TelegramAuthentication) -> String) = this.also {
        configurer.tokenResolver(resolveHandler)
    }

    fun onSuccessValidation(handler: SuccessValidationHandler) = this.also {
        configurer.onSuccessValidation(handler)
    }

    fun onSuccessValidation(successHandler: (TelegramAuthentication)-> Authentication?) = this.also {
        configurer.onSuccessValidation(successHandler)
    }

    fun and() = builder
}