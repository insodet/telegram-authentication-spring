package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.buildAuthentication
import jakarta.servlet.http.HttpServletRequest

internal class HTMLFormGeneratorTelegram: TelegramAuthenticationGenerator {
    override fun generate(request: HttpServletRequest): TelegramAuthentication =
        request
            .parameterMap
            .mapValues { entry -> entry.value.joinToString(";") }
            .let {
                request.validationFlow.buildAuthentication(it)
            }
}