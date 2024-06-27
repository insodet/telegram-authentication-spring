package com.vk.romindx.telegrambotsSpringWrapper.authentication

import com.vk.romindx.telegrambotsSpringWrapper.authentication.stringGenerator.getAuthenticationParameters
import com.vk.romindx.telegrambotsSpringWrapper.authentication.validation.ValidationFlow
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher

class TelegramAuthenticationFilter internal constructor(matcher: RequestMatcher): AbstractAuthenticationProcessingFilter(matcher) {

    internal constructor(pattern: String? = null): this(AntPathRequestMatcher(pattern ?: "/**", "POST"))

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        if (request == null) {
            throw AuthenticationError.UnexpectedError("Request can't be null")
        }
        return request
            .createTelegramPrincipal()
            ?.let { TelegramAuthentication(it, request.validationFlow) }
            ?: throw AuthenticationError.PrincipalCreationError("Error principal creation")
    }
}

private fun HttpServletRequest.createTelegramPrincipal(): TelegramPrincipal? =
    getAuthenticationParameters().let { params ->
        val hash = params["hash"]
        val username = params["username"]
        if (username == null || hash == null) {
            return@let null
        }
        TelegramPrincipal(
            username,
            hash,
            params.filter { arrayOf("hash", "username").contains(it.key.lowercase()) }
        )
    }

private val HttpServletRequest.validationFlow: ValidationFlow
    get() =
        when (getHeader("X-Source")?.lowercase() ?: "") {
            "miniapp" -> ValidationFlow.MiniApp
            else -> ValidationFlow.LoginForm
        }
