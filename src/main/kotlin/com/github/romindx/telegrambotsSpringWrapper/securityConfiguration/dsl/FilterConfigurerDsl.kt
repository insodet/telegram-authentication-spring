@file:Suppress("unused")
package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.dsl

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramFilterConfigurer
import com.github.romindx.telegrambotsSpringWrapper.toMatcher
import org.springframework.security.core.Authentication
import org.springframework.security.web.util.matcher.RequestMatcher

class FilterConfigurerDsl(private val matcher: RequestMatcher) {
    private var tokenResolver: TelegramBotTokenResolver? = null
    private var successValidationHandler: SuccessValidationHandler? = null

    constructor(pattern: String): this(pattern.toMatcher())

    fun tokenResolver(resolver: TelegramBotTokenResolver) {
        this.tokenResolver = resolver
    }

    fun onSuccessValidation(handler: SuccessValidationHandler) {
        this.successValidationHandler = handler
    }

    fun tokenResolver(resolveHandler: (TelegramAuthentication) -> String) {
        this.tokenResolver = object: TelegramBotTokenResolver {
            override fun resolve(authentication: TelegramAuthentication): String =
                resolveHandler(authentication)
        }
    }

    fun onSuccessValidation(handler: (TelegramAuthentication)-> Authentication?) {
        this.successValidationHandler = object: SuccessValidationHandler {
            override fun onSuccessValidation(authentication: TelegramAuthentication): Authentication? =
                handler(authentication)
        }
    }

    internal fun get() = TelegramFilterConfigurer(matcher).also {
        it.tokenResolver = this.tokenResolver
        it.successValidationHandler = this.successValidationHandler
    }
}