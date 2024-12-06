@file:Suppress("unused")
package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthenticationFilter
import org.springframework.security.config.annotation.SecurityConfigurer
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationBuilder<H : HttpSecurityBuilder<H>?>
    : AbstractHttpConfigurer<TelegramAuthenticationBuilder<H>?, H>() {

    internal var configs: MutableList<TelegramFilterConfigurer> = mutableListOf()

    override fun configure(builder: H) {
        builder?.also { http ->
            if (configs.isNotEmpty()) {
                http.addFilterBefore(
                    TelegramAuthenticationFilter(configs),
                    BasicAuthenticationFilter::class.java
                )
            }
        }
    }

    fun entryPoint(pattern: String) = TelegramFilterConfigurer(pattern).let {
        this.configs.add(it)
        return@let TelegramFilterBuilder(this, it)
    }

    fun entryPoint(matcher: RequestMatcher) = TelegramFilterConfigurer(matcher).let {
        this.configs.add(it)
        return@let TelegramFilterBuilder(this, it)
    }
}

fun HttpSecurity.telegramAuthentication(): TelegramAuthenticationBuilder<HttpSecurity> = getOrApply(TelegramAuthenticationBuilder())

private fun <C: SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity>> HttpSecurity.getOrApply(configurer: C) : C =
    getConfigurer(configurer.javaClass) ?: this.apply(configurer)