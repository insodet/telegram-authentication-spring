package com.vk.romindx.telegrambotsSpringWrapper.authentication.stringGenerator

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class JSONParametersGeneratorTest {
    @Test
    fun checkGenerationFromBody() {
        val request = Mockito.mock(HttpServletRequest::class.java)
        val stream =
            """
                {
                    "param": "ig-12",
                    "param2": 32
                }
            """
                .trimIndent()
                .byteInputStream()
        Mockito
            .`when`(request.inputStream)
            .thenReturn(object: ServletInputStream(){
                override fun read(): Int = stream.read()

                override fun isFinished(): Boolean = stream.available() <= 0

                override fun isReady(): Boolean = stream.available() > 0

                override fun setReadListener(p0: ReadListener?) {

                }
            })
        kotlin.test.assertEquals(
            mapOf(Pair("param", "ig-12"), Pair("param2", "32")),
            JSONParametersGenerator().generate(request)
        )
    }
}