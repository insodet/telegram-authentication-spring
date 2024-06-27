package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.dsl

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthenticationFilter
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramAuthenticationBuilder
import org.springframework.security.config.annotation.web.HttpSecurityDsl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationDsl {
    private var filter: TelegramAuthenticationFilter? = null
    private var tokenResolver: TelegramBotTokenResolver? = null
    private var successValidationHandler: SuccessValidationHandler? = null

    fun addPattern(pattern: String) {
        filter = TelegramAuthenticationFilter(pattern)
    }

    fun addMatcher(matcher: RequestMatcher) {
        filter = TelegramAuthenticationFilter(matcher)
    }

    fun tokenResolver(resolver: TelegramBotTokenResolver) {
        this.tokenResolver = resolver
    }

    fun onSuccessValidation(handler: SuccessValidationHandler) {
        this.successValidationHandler = handler
    }

    internal fun get(): TelegramAuthenticationBuilder<HttpSecurity>.() -> Unit =
        {
            this.filter = this@TelegramAuthenticationDsl.filter
            tokenResolver?.also { this.tokenResolver(it) }
            successValidationHandler?.also { this.onSuccessValidation(it) }
        }
}

fun HttpSecurityDsl.telegramAuthentication(configurer: TelegramAuthenticationDsl.()->Unit ) {
    val customizer = TelegramAuthenticationDsl().apply(configurer).get()
    this.apply(TelegramAuthenticationBuilder<HttpSecurity>(), customizer)
}