package com.github.romindx.telegrambotsSpringWrapper.authentication

import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

class TelegramPrincipal internal constructor() {
    @JsonProperty("id")
    var id: Long = 0L
        internal set
    @JsonProperty("is_bot")
    var isBot: Boolean? = null
        internal set
    @JsonProperty("first_name")
    var firstName: String = ""
        internal set
    @JsonProperty("last_name")
    var lastName: String = ""
        internal set
    @JsonProperty("username")
    var username: String = ""
        internal set
    @JsonProperty("language_code")
    var languageCode: String = ""
        internal set
    @JsonProperty("is_premium")
    var isPremium: Boolean? = null
        internal set
    @JsonProperty("added_to_attachment_menu")
    var addedToAttachmentMenu: Boolean? = null
        internal set
    @JsonProperty("allows_write_to_pm")
    var allowsWriteToPm: Boolean? = null
        internal set
    @JsonProperty("photo_url")
    var photoUrl: URI? = null
        internal set
}