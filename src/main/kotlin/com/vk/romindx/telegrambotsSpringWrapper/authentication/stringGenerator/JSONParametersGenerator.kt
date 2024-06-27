package com.vk.romindx.telegrambotsSpringWrapper.authentication.stringGenerator

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest

internal class JSONParametersGenerator: AuthenticationParametersGenerator {
    override fun generate(request: HttpServletRequest): Map<String, String> =
        ObjectMapper()
            .readValue(request.inputStream, object : TypeReference<Map<String, Any>>(){})
            .mapValues { it.value.toString() }
}