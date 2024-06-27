package com.github.romindx.telegrambotsSpringWrapper.authentication

class TelegramPrincipal internal constructor(
    val username: String,
    val hash: String,
    val attributes: Map<String, String>
    )