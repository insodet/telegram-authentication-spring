package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

internal enum class ValidationFlow {
    MiniApp, LoginForm;

    fun getAuthenticator(): TelegramAuthenticator =
        when (this) {
            MiniApp -> MiniAppValidation()
            LoginForm -> LoginFormValidation()
        }
}