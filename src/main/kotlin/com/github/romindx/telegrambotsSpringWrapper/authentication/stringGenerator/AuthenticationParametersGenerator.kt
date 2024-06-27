package com.github.romindx.telegrambotsSpringWrapper.authentication.stringGenerator

import jakarta.servlet.http.HttpServletRequest

internal interface AuthenticationParametersGenerator {
    fun generate(request: HttpServletRequest): Map<String, String>
}

internal fun HttpServletRequest.getAuthenticationParameters(): Map<String, String> =
    when  {
        contentType.contains("application/json", true) -> JSONParametersGenerator().generate(this)
        contentType.contains("application/x-www-form-urlencoded", true) -> HTMLFormParametersGenerator().generate(this)
        else -> mapOf()
    }