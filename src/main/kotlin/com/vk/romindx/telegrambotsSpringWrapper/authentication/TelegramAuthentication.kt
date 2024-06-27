package com.vk.romindx.telegrambotsSpringWrapper.authentication

import com.vk.romindx.telegrambotsSpringWrapper.authentication.validation.ValidationFlow
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class TelegramAuthentication internal constructor(
    private val principal: TelegramPrincipal,
    internal val userType: ValidationFlow
) : Authentication {

    private val authoritiesList: MutableCollection<out GrantedAuthority> = mutableListOf()

    private var authenticated = false

    override fun getName(): String = principal.username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList

    override fun getCredentials(): String = principal.hash

    override fun getDetails(): Map<String, String> = principal.attributes

    override fun getPrincipal(): TelegramPrincipal = principal

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }
}