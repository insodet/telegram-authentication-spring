package com.vk.romindx.telegrambotsSpringWrapper.authentication.stringGenerator

import jakarta.servlet.http.HttpServletRequest

internal class HTMLFormParametersGenerator: AuthenticationParametersGenerator {
    override fun generate(request: HttpServletRequest): Map<String, String> =
        request
            .parameterMap
            .mapValues { it.value.joinToString(";") }
}