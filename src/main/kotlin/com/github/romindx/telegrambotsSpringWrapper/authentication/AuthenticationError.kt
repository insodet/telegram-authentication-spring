package com.github.romindx.telegrambotsSpringWrapper.authentication

import org.springframework.security.core.AuthenticationException

sealed class AuthenticationError(msg: String): AuthenticationException(msg) {
    class UnexpectedError(msg: String): AuthenticationError(msg)
    class ValidationError(msg: String): AuthenticationError(msg)
    class DataParsingError(msg: String): AuthenticationError(msg)
}