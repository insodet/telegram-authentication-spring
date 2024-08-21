package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.buildAuthentication
import jakarta.servlet.http.HttpServletRequest

internal class JSONGeneratorTelegram: TelegramAuthenticationGenerator {
    override fun generate(request: HttpServletRequest): TelegramAuthentication? =
        request.readAuthentication()
}

private fun HttpServletRequest.readAuthentication() =
    this.validationFlow?.buildAuthentication(
        ObjectMapper()
            .readTree(this.inputStream)
            .let {node ->
                val result = HashMap<String, Any>()
                node.fields()
                    .forEach { field ->
                        result[field.key] = field.value?.let {
                            when {
                                it.isInt -> it.asInt()
                                it.isLong -> it.asLong()
                                it.isDouble -> it.asDouble()
                                it.isBoolean -> it.asBoolean()
                                it.isObject -> it.toString()
                                else -> it.asText()
                            }
                        } ?: ""
                    }
                result
            }
    )