package com.vk.romindx.telegrambotsSpringWrapper.authentication.stringGenerator

import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import kotlin.test.assertEquals

class HTMLFormParametersGeneratorTest {
    @Test
    fun checkGenerationFromBody() {
        val request = Mockito.mock(HttpServletRequest::class.java)
        Mockito
            .`when`(request.parameterMap)
            .thenReturn(mapOf(Pair("param4", arrayOf("value","1223")), Pair("para", arrayOf("3"))))
        assertEquals(
            HTMLFormParametersGenerator().generate(request),
            mapOf(Pair("param4", "value;1223"), Pair("para", "3"))
        )
    }
}