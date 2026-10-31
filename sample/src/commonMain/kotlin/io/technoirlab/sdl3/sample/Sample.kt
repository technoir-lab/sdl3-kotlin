package io.technoirlab.sdl3.sample

import io.technoirlab.sdl3.SDL_CreateWindow
import io.technoirlab.sdl3.SDL_DestroyWindow
import io.technoirlab.sdl3.SDL_EVENT_QUIT
import io.technoirlab.sdl3.SDL_Event
import io.technoirlab.sdl3.SDL_GetError
import io.technoirlab.sdl3.SDL_INIT_EVENTS
import io.technoirlab.sdl3.SDL_Init
import io.technoirlab.sdl3.SDL_Log
import io.technoirlab.sdl3.SDL_PollEvent
import io.technoirlab.sdl3.SDL_Quit
import io.technoirlab.sdl3.SDL_SetMainReady
import io.technoirlab.sdl3.SDL_WINDOW_RESIZABLE
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.toKString

class Sample : AutoCloseable {
    init {
        SDL_SetMainReady()
    }

    fun run() {
        memScoped {
            SDL_Log("Initializing SDL...")

            if (!SDL_Init(SDL_INIT_EVENTS)) {
                SDL_Log("Failed to initialize SDL: ${SDL_GetError()?.toKString()}")
                return
            }

            val window = SDL_CreateWindow(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, SDL_WINDOW_RESIZABLE)
            if (window == null) {
                SDL_Log("Failed to create window: ${SDL_GetError()?.toKString()}")
                return
            }

            var running = true
            val event = alloc<SDL_Event>()
            while (running) {
                while (SDL_PollEvent(event.ptr)) {
                    if (event.type == SDL_EVENT_QUIT) {
                        running = false
                    }
                }
            }

            SDL_DestroyWindow(window)
        }
    }

    override fun close() {
        SDL_Quit()
    }

    companion object {
        private const val WINDOW_WIDTH = 800
        private const val WINDOW_HEIGHT = 600
        private const val WINDOW_TITLE = "SDL3 Sample"
    }
}
