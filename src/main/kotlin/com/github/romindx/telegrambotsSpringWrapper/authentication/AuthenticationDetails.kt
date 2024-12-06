package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.github.romindx.telegrambotsSpringWrapper.authentication.validation.ValidationFlow

class AuthenticationDetails(val flow: ValidationFlow, val additionalParams: Map<String, Array<String>>)