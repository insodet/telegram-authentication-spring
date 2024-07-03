package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.github.romindx.telegrambotsSpringWrapper.authentication.generator.generateAuthentication
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationFilter internal constructor(
    matcher: RequestMatcher,
    authManager: AuthenticationManager
): AbstractAuthenticationProcessingFilter(matcher, authManager) {
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication =
        request
            ?.generateAuthentication()
            ?.let { authenticationManager?.authenticate(it) }
            ?: throw AuthenticationError.UnexpectedError("Request can't be null")
}
