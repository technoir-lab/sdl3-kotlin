package io.technoirlab.sdl3.sample.android

import org.libsdl.app.SDLActivity

/**
 * Runs the sample with SDL's Java glue.
 */
class SampleActivity : SDLActivity() {
    // SDL is linked statically into libsample.so, and SDLActivity looks up SDL_main in the last library listed here.
    override fun getLibraries(): Array<String> = arrayOf("sample")
}
