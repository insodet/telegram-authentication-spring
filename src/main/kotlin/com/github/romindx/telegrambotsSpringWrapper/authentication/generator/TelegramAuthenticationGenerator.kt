package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.github.romindx.telegrambotsSpringWrapper.authentication.AuthenticationError
import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.validation.ValidationFlow
import jakarta.servlet.http.HttpServletRequest

internal interface TelegramAuthenticationGenerator {
    fun generate(request: HttpServletRequest): TelegramAuthentication
}

internal fun HttpServletRequest.generateAuthentication(): TelegramAuthentication =
    try {
        when  {
            contentType?.contains("application/json", true) ?: false -> JSONGeneratorTelegram().generate(this)
            contentType?.contains("application/x-www-form-urlencoded", true) ?: false -> HTMLFormGeneratorTelegram().generate(this)
            else -> throw AuthenticationError.DataParsingError("Undefined request type")
        }
    } catch (ex: Exception) {
        throw AuthenticationError.DataParsingError(ex.localizedMessage ?: ex.message ?: "")
    }

internal val HttpServletRequest.validationFlow: ValidationFlow
    get() =
        when (getHeader("X-Source")?.lowercase() ?: "") {
            "miniapp" -> ValidationFlow.MiniApp
            else -> ValidationFlow.LoginForm
        }