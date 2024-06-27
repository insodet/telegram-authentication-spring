package com.vk.romindx.telegrambotsSpringWrapper

import com.vk.romindx.telegrambotsSpringWrapper.authentication.TelegramPrincipal
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec




internal val String.sha256hash: ByteArray
	get() {
		val bytes = this.toByteArray()
		val md = MessageDigest.getInstance("SHA-256")
		return md.digest(bytes)
	}

internal fun String.hmacSha256String(secretKey: ByteArray): String = hmacSha256(secretKey).toHexDecimal()

internal fun String.hmacSha256(secretKey: ByteArray): ByteArray =
	Mac.getInstance("HmacSHA256")
		.also { it.init(SecretKeySpec(secretKey, "HmacSHA256")) }
		.doFinal(this.toByteArray())


internal fun ByteArray.toHexDecimal() = this.fold("") { result, value -> result + "%02x".format(value)}

internal fun Map<String, String>.toCheckString() =
	this
		.keys
		.sorted()
		.joinToString("\n") { "${it}=${this[it] ?: ""}" }

internal fun TelegramPrincipal.getCheckAttributes() =
	this
		.attributes
		.toMutableMap()
		.also { it["username"] = this.username }