package com.github.romindx.telegrambotsSpringWrapper.authentication.validation

enum class ValidationFlow {
    MiniApp, LoginForm;

    internal fun getAuthenticator(): TelegramAuthenticator =
        when (this) {
            MiniApp -> MiniAppValidation()
            LoginForm -> LoginFormValidation()
        }
}