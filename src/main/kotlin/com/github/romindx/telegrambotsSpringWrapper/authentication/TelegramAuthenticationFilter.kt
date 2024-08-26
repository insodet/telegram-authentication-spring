package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.github.romindx.telegrambotsSpringWrapper.authentication.generator.generateAuthentication
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.web.filter.OncePerRequestFilter

class TelegramAuthenticationFilter(
    private val matcher: RequestMatcher,
    private val authManager: AuthenticationManager
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (matcher.matches(request)) {
            request
                .generateAuthentication()
                ?.let { authManager.authenticate(it) }
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
