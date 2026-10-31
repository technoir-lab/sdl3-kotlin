package io.technoirlab.sdl3

import kotlinx.cinterop.toKStringFromUtf8
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SDLInitTest {
    @BeforeTest
    fun setUp() {
        SDL_SetMainReady()
    }

    @AfterTest
    fun tearDown() {
        SDL_Quit()
    }

    @Test
    fun initialization() {
        val result = SDL_Init(0u)
        if (!result) {
            println("SDL_Init Error: ${SDL_GetError()?.toKStringFromUtf8()}")
        }
        assertTrue(result)
    }
}
