package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.github.romindx.telegrambotsSpringWrapper.authentication.generator.generateAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.provider.TelegramAuthenticationProvider
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramFilterConfigurer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class TelegramAuthenticationFilter(
    private val filterConfigurations: List<TelegramFilterConfigurer>
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        filterConfigurations
            .firstOrNull { it.matcher.matches(request) }
            ?.let { configuration ->
            request
                .generateAuthentication()
                ?.let { configuration.authenticator.authenticate(it) }
                ?.let {
                    if (it.isAuthenticated) {
                        SecurityContextHolder.getContext().authentication = it
                    }
                }
                ?: throw AuthenticationError.UnexpectedError("Request can't be null")
        }
        filterChain.doFilter(request, response)
    }

}

private val TelegramFilterConfigurer.authenticator: TelegramAuthenticationProvider
    get() = TelegramAuthenticationProvider(this.tokenResolver, this.successValidationHandler)
