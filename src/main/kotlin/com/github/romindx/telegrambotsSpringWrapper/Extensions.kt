package com.github.romindx.telegrambotsSpringWrapper

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.net.URLDecoder
import java.security.MessageDigest
import java.util.HashMap
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal fun String.toMatcher() = AntPathRequestMatcher(this, "POST")

internal val HttpServletRequest.queryParameters: Map<String, Array<String>>
	get() = HashMap<String, Array<String>>().also { resultMap ->
		Regex("([^\\s?#&=]+)=([^\\s?#&=]+)")
			.findAll(this.queryString ?: "")
			.toList()
			.map { match->
				val pair = match.value.split("=")
				pair.getOrNull(0)?.let { key->
					pair.getOrNull(1)?.let { value->
						val res = resultMap[key]?.toMutableList() ?: mutableListOf()
						res.add(URLDecoder.decode(value, Charsets.UTF_8))
						resultMap[key] = res.toTypedArray()
					}
				}
			}
	}

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