package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.romindx.telegrambotsSpringWrapper.authentication.validation.ValidationFlow
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.net.URI
import java.util.Date

class TelegramAuthentication private constructor() : Authentication {

    private val authoritiesList: MutableCollection<out GrantedAuthority> = mutableListOf()
    private var authenticated = false
    private lateinit var hash: String
    private lateinit var telegramPrincipal: TelegramPrincipal
    internal var authDate: Date? = null
        private set
    internal lateinit var checkString: String
        private set
    internal lateinit var validationFlow: ValidationFlow
        private set
    override fun getName(): String = principal.username
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authoritiesList
    override fun getCredentials(): String = hash
    override fun getDetails(): ValidationFlow = validationFlow
    override fun getPrincipal(): TelegramPrincipal = telegramPrincipal
    override fun isAuthenticated(): Boolean = authenticated
    override fun setAuthenticated(isAuthenticated: Boolean) {
        authenticated = isAuthenticated
    }

    internal class Builder {
        val result = TelegramAuthentication()

        fun addHash(hash: String): Builder =
            this.also {
                result.hash = hash
            }

        fun addAuthDate(date: Date?): Builder =
            this.also {
                result.authDate = date
            }

        fun addCheckString(str: String): Builder =
            this.also {
                result.checkString = str
            }

        fun addValidationFlow(flow: ValidationFlow): Builder =
            this.also {
                result.validationFlow = flow
            }

        fun addPrincipal(tgPrincipal: TelegramPrincipal): Builder =
            this.also {
                result.telegramPrincipal = tgPrincipal
            }
        fun build() = result
    }

}

internal fun ValidationFlow.buildAuthentication(parameters: Map<String, Any>) = TelegramAuthentication
    .Builder()
    .addHash(parameters["hash"] as? String ?: "")
    .addValidationFlow(this)
    .addAuthDate(parameters["auth_date"]
        ?.let {
            when(it) {
                is String -> it.toLong()
                is Int -> it.toLong()
                is Long -> it
                else -> null
            }
        }
        ?.let { Date(it * 1000) })
    .addCheckString(parameters
        .keys
        .sorted()
        .filter { it != "hash" }
        .joinToString("\n") { "${it}=${parameters[it] as? String ?: parameters[it] ?: ""}" })
    .addPrincipal(this.readPrincipal(parameters))
    .build()

private fun ValidationFlow.readPrincipal(parameters: Map<String, Any>): TelegramPrincipal =
    when (this) {
        ValidationFlow.LoginForm -> getPrincipalFrom(parameters)
        ValidationFlow.MiniApp -> getPrincipalFrom(parameters["user"] ?: "")
    } ?: throw AuthenticationError.DataParsingError("Principal read error")

private fun getPrincipalFrom(value: Any): TelegramPrincipal? =
    when (value) {
        is String -> ObjectMapper().readValue(value, TelegramPrincipal::class.java)
        is Map<*, *> -> TelegramPrincipal().apply {
            lastName = value["last_name"] as? String ?: ""
            id = value["id"]?.let {
                it as? Long ?: (it as? String)?.toLong()
            } ?: 0
            isBot = value["is_bot"]?.let {
                it as? Boolean ?: (it as? String)?.readBoolean()
            }
            firstName = value["first_name"] as? String ?: ""
            lastName = value["last_name"] as? String ?: ""
            username = value["username"] as? String ?: ""
            languageCode = value["language_code"] as? String ?: ""
            isPremium = value["is_premium"]?.let {
                it as? Boolean ?: (it as? String)?.readBoolean()
            }
            addedToAttachmentMenu = value["added_to_attachment_menu"]?.let {
                it as? Boolean ?: (it as? String)?.readBoolean()
            }
            allowsWriteToPm = value["allows_write_to_pm"]?.let {
                it as? Boolean ?: (it as? String)?.readBoolean()
            }
            photoUrl = (value["photo_url"] as? String)?.let { URI(it) }
        }
        else -> null
    }

private fun String.readBoolean(): Boolean? =
    when (this.lowercase()) {
        "true", "1" -> true
        "false", "0" -> false
        else -> null
    }