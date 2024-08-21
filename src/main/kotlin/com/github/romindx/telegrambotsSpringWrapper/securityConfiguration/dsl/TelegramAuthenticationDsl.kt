package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.dsl

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramAuthenticationBuilder
import org.springframework.security.config.annotation.web.HttpSecurityDsl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationDsl {
    private var matcher: RequestMatcher? = null
    private var tokenResolver: TelegramBotTokenResolver? = null
    private var successValidationHandler: SuccessValidationHandler? = null

    fun addPattern(pattern: String) {
        matcher = AntPathRequestMatcher(pattern, "POST")
    }

    fun addMatcher(matcher: RequestMatcher) {
        this.matcher = matcher
    }

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

    internal fun get(): TelegramAuthenticationBuilder<HttpSecurity>.() -> Unit =
        {
            this.matcher = this@TelegramAuthenticationDsl.matcher
            tokenResolver?.also { this.tokenResolver(it) }
            successValidationHandler?.also { this.onSuccessValidation(it) }
        }
}

fun HttpSecurityDsl.telegramAuthentication(configurer: TelegramAuthenticationDsl.()->Unit ) {
    val customizer = TelegramAuthenticationDsl().apply(configurer).get()
    this.apply(TelegramAuthenticationBuilder<HttpSecurity>(), customizer)
}