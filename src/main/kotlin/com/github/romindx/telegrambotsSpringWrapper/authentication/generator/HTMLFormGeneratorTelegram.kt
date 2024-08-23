package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.buildAuthentication
import jakarta.servlet.http.HttpServletRequest
import java.net.URLDecoder

internal class HTMLFormGeneratorTelegram: TelegramAuthenticationGenerator {
    override fun generate(request: HttpServletRequest): TelegramAuthentication? =
        request
            .inputStream
            .bufferedReader()
            .use {
                it.readText()
            }.split("&")
            .associate { pair ->
                val parts = pair.split("=").map { URLDecoder.decode(it, Charsets.UTF_8) }
                Pair(parts[0], parts.getOrNull(1) ?: "")
            }
            .let {
                request.validationFlow?.buildAuthentication(it)
            }
}