package com.github.romindx.telegrambotsSpringWrapper.authentication

import org.springframework.security.access.AccessDeniedException

sealed class AuthenticationError(msg: String): AccessDeniedException(msg) {
    class UnexpectedError(msg: String): AuthenticationError(msg)
    class ValidationError(msg: String): AuthenticationError(msg)
    class DataParsingError(msg: String): AuthenticationError(msg)
}