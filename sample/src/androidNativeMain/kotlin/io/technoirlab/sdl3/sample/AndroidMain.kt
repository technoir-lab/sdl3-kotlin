package io.technoirlab.sdl3.sample

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointerVar
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.CName

/**
 * Entry point on Android, where SDLActivity looks up `SDL_main` in this library and runs it with `SDL_RunApp` instead of [main].
 * It must stay public: Kotlin/Native drops non-public `@CName` functions from shared libraries.
 */
@OptIn(ExperimentalNativeApi::class)
@CName("SDL_main")
fun androidMain(argc: Int, argv: CArrayPointer<CPointerVar<ByteVar>>?): Int = sdlMain(argc, argv)
