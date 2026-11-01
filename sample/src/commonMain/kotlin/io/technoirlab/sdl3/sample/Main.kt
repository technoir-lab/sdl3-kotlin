package io.technoirlab.sdl3.sample

import cnames.structs.SDL_Renderer
import cnames.structs.SDL_Window
import io.technoirlab.sdl3.SDL_ALPHA_OPAQUE_FLOAT
import io.technoirlab.sdl3.SDL_CreateWindowAndRenderer
import io.technoirlab.sdl3.SDL_DestroyRenderer
import io.technoirlab.sdl3.SDL_DestroyWindow
import io.technoirlab.sdl3.SDL_EVENT_QUIT
import io.technoirlab.sdl3.SDL_Event
import io.technoirlab.sdl3.SDL_GetError
import io.technoirlab.sdl3.SDL_INIT_EVENTS
import io.technoirlab.sdl3.SDL_Init
import io.technoirlab.sdl3.SDL_Log
import io.technoirlab.sdl3.SDL_PollEvent
import io.technoirlab.sdl3.SDL_Quit
import io.technoirlab.sdl3.SDL_RenderClear
import io.technoirlab.sdl3.SDL_RenderPresent
import io.technoirlab.sdl3.SDL_RunApp
import io.technoirlab.sdl3.SDL_SetRenderDrawColorFloat
import io.technoirlab.sdl3.SDL_SetRenderVSync
import io.technoirlab.sdl3.SDL_WINDOW_RESIZABLE
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CPointerVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.staticCFunction
import kotlinx.cinterop.value
import kotlin.system.exitProcess

private const val WINDOW_WIDTH = 800
private const val WINDOW_HEIGHT = 600
private const val WINDOW_TITLE = "SDL3 Sample"

fun main() {
    val status = memScoped {
        // On iOS and tvOS, SDL passes argv to UIApplicationMain, which requires a non-null argv.
        val argv = allocArrayOf("sample".cstr.ptr, null)
        SDL_RunApp(1, argv, staticCFunction(::sdlMain), null)
    }
    exitProcess(status)
}

internal fun sdlMain(argc: Int, argv: CArrayPointer<CPointerVar<ByteVar>>?): Int {
    try {
        runSample()
    } finally {
        SDL_Quit()
    }
    return 0
}

private fun runSample() {
    memScoped {
        SDL_Log("Initializing SDL...")

        if (!SDL_Init(SDL_INIT_EVENTS)) {
            SDL_Log("Failed to initialize SDL: %s", SDL_GetError())
            return
        }

        val window = alloc<CPointerVar<SDL_Window>>()
        val renderer = alloc<CPointerVar<SDL_Renderer>>()
        if (!SDL_CreateWindowAndRenderer(WINDOW_TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, SDL_WINDOW_RESIZABLE, window.ptr, renderer.ptr)) {
            SDL_Log("Failed to create window and renderer: %s", SDL_GetError())
            return
        }
        SDL_SetRenderVSync(renderer.value, 1)

        var running = true
        val event = alloc<SDL_Event>()
        while (running) {
            while (SDL_PollEvent(event.ptr)) {
                if (event.type == SDL_EVENT_QUIT) {
                    running = false
                }
            }
            drawFrame(renderer.value)
        }

        SDL_DestroyRenderer(renderer.value)
        SDL_DestroyWindow(window.value)
    }
}

private fun drawFrame(renderer: CPointer<SDL_Renderer>?) {
    SDL_SetRenderDrawColorFloat(renderer, 1f, 0f, 1f, SDL_ALPHA_OPAQUE_FLOAT) // Magenta
    SDL_RenderClear(renderer)
    SDL_RenderPresent(renderer)
}
