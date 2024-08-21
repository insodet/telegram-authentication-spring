package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.validation.TelegramAuthenticator
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URLDecoder
import java.util.*

class MiniAppGeneratorTest {

    val formData = "query_id=AAGJRCMDAQAAAIlEIwMfLofu&user=%7B%22id%22%3A2200126601%2C%22first_name%22%3A%22%D0%A0%D1%83%D0%B1%D0%B8%D0%BA%22%2C%22last_name%22%3A%22%D0%93%D0%BE%D0%BB%D0%BE%D0%B2%D0%B8%D0%BD%22%2C%22language_code%22%3A%22en%22%2C%22allows_write_to_pm%22%3Atrue%7D&auth_date=1720027177&hash=a07f96e3f2675a002ff059e9de0e4b4f79f571ec502c914a5cb81653726596b5"
    val jsonData = """
        {"query_id":"AAGJRCMDAQAAAIlEIwMfLofu","user":{"id":2200126601,"first_name":"Рубик","last_name":"Головин","language_code":"en","allows_write_to_pm":true},"auth_date":"1720027177","hash":"a07f96e3f2675a002ff059e9de0e4b4f79f571ec502c914a5cb81653726596b5"}
    """.trimIndent()
    @Test
    fun jsonAuthenticationGeneratorAndValidationTest() {
        val request = jsonRequest()
        Mockito
            .`when`(request.getHeader("X-Flow"))
            .thenReturn("MiniApp")
        val result = JSONGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.validationFlow.getAuthenticator(), result)
    }

    @Test
    fun htmlFormAuthenticationGeneratorAndValidationTest() {
        val request = formRequest()
        Mockito
            .`when`(request.getHeader("X-Flow"))
            .thenReturn("MiniApp")
        val result = HTMLFormGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.validationFlow.getAuthenticator(), result)
    }

    @Test
    fun jsonAuthenticationGeneratorAndValidationWithQueryFlowTest() {
        val request = jsonRequest()
        Mockito
            .`when`(request.getParameter("flow"))
            .thenReturn("MiniApp")
        val result = JSONGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.validationFlow.getAuthenticator(), result)
    }

    @Test
    fun htmlFormAuthenticationGeneratorAndValidationWithQueryFlowTest() {
        val request = formRequest()
        Mockito
            .`when`(request.getParameter("flow"))
            .thenReturn("MiniApp")
        val result = HTMLFormGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.validationFlow.getAuthenticator(), result)
    }

    private fun jsonRequest(): HttpServletRequest {
        val request = Mockito.mock(HttpServletRequest::class.java)
        val stream = jsonData.byteInputStream()
        Mockito
            .`when`(request.inputStream)
            .thenReturn(object: ServletInputStream() {
                override fun read(): Int = stream.read()

                override fun isFinished(): Boolean = stream.available() <= 0

                override fun isReady(): Boolean = stream.available() > 0

                override fun setReadListener(p0: ReadListener?) {}

            })
        return request
    }

    private fun formRequest(): HttpServletRequest {
        val request = Mockito.mock(HttpServletRequest::class.java)
        Mockito.`when`(request.parameterMap)
            .thenReturn(
                formData
                    .split("&")
                    .associate { pair ->
                        val keyVal = pair.split("=")
                        Pair<String, Array<String>>(
                            URLDecoder.decode(keyVal.firstOrNull() ?: "", Charsets.UTF_8),
                            arrayOf(URLDecoder.decode(keyVal.getOrNull(1) ?: "", Charsets.UTF_8))
                        )
                    }
            )
        return request
    }

    private fun validationTest(validator: TelegramAuthenticator, authentication: TelegramAuthentication) {
        assertThat(
            validator.isValid(authentication, "2200445336:AAG0XGyLDgCHathLCdEhFJNprPsE6lesoSU"),
            `is`(true)
        )
    }

    private fun authenticationTest(authentication: TelegramAuthentication) {
        assertThat(
            authentication.credentials,
            `is`("a07f96e3f2675a002ff059e9de0e4b4f79f571ec502c914a5cb81653726596b5")
        )
        assertThat(authentication.authDate, `is`(Date(1720027177000)))
        assertThat(authentication.principal.allowsWriteToPm, `is`(true))
        assertThat(authentication.principal.id, `is`(2200126601))
        assertThat(authentication.principal.firstName, `is`("Рубик"))
        assertThat(authentication.principal.lastName, `is`("Головин"))
        assertThat(authentication.principal.languageCode, `is`("en"))
    }
}