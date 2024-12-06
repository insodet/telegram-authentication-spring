package com.github.romindx.telegrambotsSpringWrapper.authentication.generator

import com.github.romindx.telegrambotsSpringWrapper.authentication.TelegramAuthentication
import com.github.romindx.telegrambotsSpringWrapper.authentication.validation.TelegramAuthenticator
import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.net.URI
import java.util.*

class LoginFormGenerationsTests {

    private val jsonData = """
        {"id":6519942610,"first_name":"${"$"}SCUF","last_name":"${"$"}PONKE","username":"userkakiev","photo_url":"https://t.me/i/userpic/320/PTwgsZVG6RNmI4L919LvUmK7lNhTfNsUm3WBzC_bKm__QeRcw578ub7bG5m8dpb-.jpg","auth_date":1720017139,"hash":"7ed52b0f9d35ca0b450d6532c34bf0a6312572abd4c171c2c997eb392f9ff035"}
    """.trimIndent()

    private val formEncoded = "id=6519942610&first_name=%24SCUF&last_name=%24PONKE&username=userkakiev&photo_url=https%3A%2F%2Ft%2Eme%2Fi%2Fuserpic%2F320%2FPTwgsZVG6RNmI4L919LvUmK7lNhTfNsUm3WBzC%5FbKm%5F%5FQeRcw578ub7bG5m8dpb%2D%2Ejpg&auth_date=1720017139&hash=7ed52b0f9d35ca0b450d6532c34bf0a6312572abd4c171c2c997eb392f9ff035"

    @Test
    fun jsonAuthenticationGeneratorAndValidationTest() {
        val request = request(jsonData)
        Mockito
            .`when`(request.getHeader("X-Flow"))
            .thenReturn("LoginForm")
        val result = JSONGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.details.flow.getAuthenticator(), result)
    }

    @Test
    fun jsonAuthenticationGeneratorAndValidationWithQueryTest() {
        val request = request(jsonData)
        Mockito
            .`when`(request.getParameter("flow"))
            .thenReturn("LoginForm")
        val result = JSONGeneratorTelegram().generate(request)
        authenticationTest(result!!)
        validationTest(result.details.flow.getAuthenticator(), result)
    }

    private fun validationTest(validator: TelegramAuthenticator, authentication: TelegramAuthentication) {
        assertThat(
            validator.isValid(authentication, "6741227094:AAEigII-St94nLYPDiz_NkILA1DZesFKe38"),
            `is`(true)
        )
    }

    private fun authenticationTest(authentication: TelegramAuthentication) {
        assertThat(authentication.credentials, `is`("7ed52b0f9d35ca0b450d6532c34bf0a6312572abd4c171c2c997eb392f9ff035"))
        assertThat(authentication.authDate, `is`(Date(1720017139000)))
        assertThat(authentication.principal.username, `is`("userkakiev"))
        assertThat(authentication.principal.id, `is`(6519942610))
        assertThat(authentication.principal.firstName, `is`("\$SCUF"))
        assertThat(authentication.principal.lastName, `is`("\$PONKE"))
        assertThat(authentication.principal.photoUrl, `is`(URI("https://t.me/i/userpic/320/PTwgsZVG6RNmI4L919LvUmK7lNhTfNsUm3WBzC_bKm__QeRcw578ub7bG5m8dpb-.jpg")))
    }

    @Test
    fun htmlFormAuthenticationGeneratorAndValidationTest() {
        val request = request(formEncoded)
        Mockito.`when`(request.getHeader("X-Flow")).thenReturn("LoginForm")
        val authentication = HTMLFormGeneratorTelegram().generate(request)
        authenticationTest(authentication!!)
        validationTest(authentication.details.flow.getAuthenticator(), authentication)
    }

    @Test
    fun queryParametersTest() {
        val request = request(formEncoded)
        Mockito.`when`(request.queryString).thenReturn("text=арутюнов+%25%3A№ПУУ%3AН&win=666&lr=213&clid=1955453&win=672#garbalu")
        Mockito.`when`(request.getParameter("flow")).thenReturn("LoginForm")
        val expected = mapOf(
            "text" to arrayOf("арутюнов %:№ПУУ:Н"),
            "lr" to arrayOf("213"),
            "clid" to arrayOf("1955453"),
            "win" to arrayOf("666","672")
        )
        val authentication = HTMLFormGeneratorTelegram().generate(request)
        expected
            .entries
            .forEach{
                assertThat(authentication!!.details.additionalParams[it.key], equalTo(it.value))
            }
        assertThat(authentication!!.details.additionalParams.size, `is`(expected.size))
    }

    @Test
    fun htmlFormAuthenticationGeneratorAndValidationWithQueryTest() {
        val request = request(formEncoded)
        Mockito.`when`(request.getParameter("flow")).thenReturn("LoginForm")
        val authentication = HTMLFormGeneratorTelegram().generate(request)
        authenticationTest(authentication!!)
        validationTest(authentication.details.flow.getAuthenticator(), authentication)
    }

    private fun request(body: String): HttpServletRequest {
        val request = Mockito.mock(HttpServletRequest::class.java)
        val stream = body.byteInputStream()
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
}