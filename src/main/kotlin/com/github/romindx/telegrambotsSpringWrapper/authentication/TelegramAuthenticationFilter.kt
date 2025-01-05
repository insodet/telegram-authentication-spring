package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.github.romindx.telegrambotsSpringWrapper.authentication.generator.generateAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.provider.TelegramAuthenticationProvider
import com.github.romindx.telegrambotsSpringWrapper.securityConfiguration.builders.TelegramFilterConfigurer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.context.SecurityContextHolderStrategy
import org.springframework.security.web.context.HttpSessionSecurityContextRepository
import org.springframework.security.web.context.SecurityContextRepository
import org.springframework.web.filter.OncePerRequestFilter

class TelegramAuthenticationFilter(
    private val filterConfigurations: List<TelegramFilterConfigurer>,
): OncePerRequestFilter() {

    private var securityContextRepository: SecurityContextRepository = HttpSessionSecurityContextRepository()
    private val securityContextHolderStrategy: SecurityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        try {
            filterConfigurations
                .firstOrNull { it.matcher.matches(request) }
                ?.let { configuration ->
                    request
                        .generateAuthentication()
                        ?.let { configuration.authenticator.authenticate(it) }
                        ?.let {
                            if (it.isAuthenticated) {
                                val newContext = securityContextHolderStrategy.createEmptyContext()
                                newContext.authentication = it
                                securityContextRepository.saveContext(newContext, request, response)
                            }
                        }
                        ?: throw AuthenticationError.UnexpectedError("Request can't be null")
                }
        } catch (exception: Exception) {
            securityContextHolderStrategy.clearContext()
            throw exception
        }
        filterChain.doFilter(request, response)
    }

    @Suppress("unused")
    fun setSecurityContextRepository(securityContextRepository: SecurityContextRepository) {
        this.securityContextRepository = securityContextRepository
    }

}

private val TelegramFilterConfigurer.authenticator: TelegramAuthenticationProvider
    get() = TelegramAuthenticationProvider(this.tokenResolver, this.successValidationHandler)
