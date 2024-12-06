package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.github.romindx.telegrambotsSpringWrapper.toMatcher
import org.springframework.security.core.Authentication
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramFilterConfigurer(internal val matcher: RequestMatcher) {
    constructor(pattern: String) : this(pattern.toMatcher())

    internal var tokenResolver: TelegramBotTokenResolver? = null
    internal var successValidationHandler: SuccessValidationHandler? = null

    fun tokenResolver(resolver: TelegramBotTokenResolver) = this.also {
        it.tokenResolver = resolver
    }

    fun tokenResolver(resolveHandler: (TelegramAuthentication) -> String) =
        this.also {
            it.tokenResolver = object: TelegramBotTokenResolver {
                override fun resolve(authentication: TelegramAuthentication): String =
                    resolveHandler(authentication)
            }
        }

    fun onSuccessValidation(handler: SuccessValidationHandler) = this.also {
        it.successValidationHandler = handler
    }

    fun onSuccessValidation(successHandler: (TelegramAuthentication)-> Authentication?) =
        this.also {
            it.successValidationHandler = object: SuccessValidationHandler {
                override fun onSuccessValidation(authentication: TelegramAuthentication): Authentication? =
                    successHandler(authentication)
            }
        }
}