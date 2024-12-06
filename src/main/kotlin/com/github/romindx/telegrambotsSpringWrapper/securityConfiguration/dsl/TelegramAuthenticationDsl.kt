@file:Suppress("unused")
package com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.dsl

import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramAuthenticationBuilder
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramFilterConfigurer
import org.springframework.security.config.annotation.web.HttpSecurityDsl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationDsl {
    private var configs: MutableList<TelegramFilterConfigurer> = mutableListOf()

    fun entryPoint(pattern: String, configurer: FilterConfigurerDsl.() -> Unit) {
        configs.add(FilterConfigurerDsl(pattern).apply(configurer).get())
    }

    fun entryPoint(matcher: RequestMatcher, configurer: FilterConfigurerDsl.() -> Unit) {
        configs.add(FilterConfigurerDsl(matcher).apply(configurer).get())
    }

    internal fun get(): TelegramAuthenticationBuilder<HttpSecurity>.() -> Unit =
        {
            this.configs = this@TelegramAuthenticationDsl.configs
        }
}

fun HttpSecurityDsl.telegramAuthentication(configurer: TelegramAuthenticationDsl.()->Unit ) {
    val customizer = TelegramAuthenticationDsl().apply(configurer).get()
    this.apply(TelegramAuthenticationBuilder<HttpSecurity>(), customizer)
}