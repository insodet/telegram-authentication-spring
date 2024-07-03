package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthenticationFilter
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.github.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.github.romindx.telegrambotsSpringWrapper.authentication.provider.TelegramAuthenticationProvider
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationBuilder<H : HttpSecurityBuilder<H>?>()
    : AbstractHttpConfigurer<TelegramAuthenticationBuilder<H>?, H>() {

    constructor(matcher: RequestMatcher): this() {
        this.matcher = matcher
    }

    internal var matcher: RequestMatcher? = null
    private var tokenResolver: TelegramBotTokenResolver? = null
    private var successValidationHandler: SuccessValidationHandler? = null

    override fun configure(builder: H) {
        builder?.also { http ->
            http.authenticationProvider(TelegramAuthenticationProvider(tokenResolver, successValidationHandler))
            val authenticationManager = http
                .getSharedObject(AuthenticationManager::class.java)
            matcher?.also {
                http.addFilterBefore(
                    TelegramAuthenticationFilter(it, authenticationManager),
                    BasicAuthenticationFilter::class.java
                )
            }
        }
    }

    fun tokenResolver(resolver: TelegramBotTokenResolver) =
        this.also { it.tokenResolver = resolver }

    fun tokenResolver(resolveHandler: (TelegramAuthentication) -> String) =
        this.also {
            it.tokenResolver = object: TelegramBotTokenResolver {
                override fun resolve(authentication: TelegramAuthentication): String =
                    resolveHandler(authentication)
            }
        }

    fun onSuccessValidation(handler: SuccessValidationHandler) =
        this.also { it.successValidationHandler = handler }

    fun onSuccessValidation(successHandler: (TelegramAuthentication)-> Authentication) =
        this.also {
            it.successValidationHandler = object: SuccessValidationHandler {
                override fun onSuccessValidation(authentication: TelegramAuthentication): Authentication =
                    successHandler(authentication)
            }
        }
}

fun HttpSecurity.telegramAuthentication(pattern: String): TelegramAuthenticationBuilder<HttpSecurity>
= getOrApply(TelegramAuthenticationBuilder(AntPathRequestMatcher(pattern, "POST")))

fun HttpSecurity.telegramAuthentication(matcher: RequestMatcher): TelegramAuthenticationBuilder<HttpSecurity>
        = getOrApply(TelegramAuthenticationBuilder(matcher))

private fun <C: SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> HttpSecurity.getOrApply(configurer: C) : C =
    getConfigurer(configurer.javaClass) ?: this.apply(configurer)