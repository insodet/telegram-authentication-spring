package com.vk.romindx.telegrambotsSpringWrapper.securityConfiguration.builders

import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthenticationFilter
import com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers.SuccessValidationHandler
import com.vk.romindx.telegrambotsSpringWrapper.authentication.handlers.TelegramBotTokenResolver
import com.vk.romindx.telegrambotsSpringWrapper.authentication.provider.TelegramAuthenticationProvider
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationBuilder<H : HttpSecurityBuilder<H>?>()
    : AbstractHttpConfigurer<TelegramAuthenticationBuilder<H>?, H>() {

    constructor(filter: TelegramAuthenticationFilter): this() {
        this.filter = filter
    }

    internal var filter: TelegramAuthenticationFilter? = null
    private var tokenResolver: TelegramBotTokenResolver? = null
    private var successValidationHandler: SuccessValidationHandler? = null

    override fun configure(builder: H) {
        builder?.authenticationProvider(TelegramAuthenticationProvider(tokenResolver, successValidationHandler))
        builder?.addFilterBefore(filter, BasicAuthenticationFilter::class.java)
    }

    fun tokenResolver(resolver: TelegramBotTokenResolver) =
        this.also { it.tokenResolver = resolver }

    fun onSuccessValidation(handler: SuccessValidationHandler) =
        this.also { it.successValidationHandler = handler }
}

fun HttpSecurity.telegramAuthentication(pattern: String): TelegramAuthenticationBuilder<HttpSecurity>
= getOrApply(TelegramAuthenticationBuilder(TelegramAuthenticationFilter(pattern)))

fun HttpSecurity.telegramAuthentication(matcher: RequestMatcher): TelegramAuthenticationBuilder<HttpSecurity>
        = getOrApply(TelegramAuthenticationBuilder(TelegramAuthenticationFilter(matcher)))

private fun <C: SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> HttpSecurity.getOrApply(configurer: C) : C =
    getConfigurer(configurer.javaClass) ?: this.apply(configurer)