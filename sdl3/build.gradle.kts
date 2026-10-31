import io.technoirlab.cmake.import.tasks.CMakeGenerateCInteropDefinitionTask
import io.technoirlab.cmake.import.tasks.CMakeGenerateTask
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.KonanTarget

plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-library")
    id("io.technoirlab.cmake-import")
}

kotlinMultiplatformLibrary {
    packageName = "io.technoirlab.sdl3"
}

kotlin {
    androidNativeArm64()
    iosArm64()
    iosSimulatorArm64()
    linuxArm64()
    linuxX64()
    macosArm64()
    mingwX64()
    tvosArm64()
    tvosSimulatorArm64()

    compilerOptions {
        optIn.add("kotlinx.cinterop.ExperimentalForeignApi")
    }

    sourceSets {
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

cmakeImport {
    sourceDirectory = layout.projectDirectory.dir("../thirdparty/SDL")
    targetName = "SDL3-static"
    packageName = "io.technoirlab.sdl3"
    headers.addAll("SDL3/SDL.h", "SDL3/SDL_main.h")
    defines.putAll(
        mapOf(
            "CMAKE_DISABLE_PRECOMPILE_HEADERS" to "ON",
            "CMAKE_POSITION_INDEPENDENT_CODE" to "ON",
            "SDL_ANDROID_JAR" to "OFF",
            "SDL_EXAMPLES" to "OFF",
            "SDL_INSTALL" to "ON",
            "SDL_INSTALL_CPACK" to "OFF",
            "SDL_INSTALL_DOCS" to "OFF",
            "SDL_SHARED" to "OFF",
            "SDL_STATIC" to "ON",
            "SDL_TEST_LIBRARY" to "OFF",
            "SDL_TESTS" to "OFF",
        ),
    )
}

kotlin.targets.withType<KotlinNativeTarget>().configureEach {
    val taskName = "cmakeGenerateCInteropDefinition${name.replaceFirstChar(Char::uppercaseChar)}"
    val graphicsHeader = if (konanTarget.family.isAppleFamily) "SDL3/SDL_metal.h" else "SDL3/SDL_vulkan.h"
    tasks.named<CMakeGenerateCInteropDefinitionTask>(taskName) {
        includedHeaders.add(graphicsHeader)
    }
}

tasks.withType<CMakeGenerateTask>().configureEach {
    if (konanTarget.get().family == Family.LINUX) {
        cmakeDefines.put("SDL_UNIX_CONSOLE_BUILD", "ON")
    }

    if (konanTarget.get() != KonanTarget.MINGW_X64) return@configureEach

    cmakeDefines.putAll(
        mapOf(
            "CMAKE_CXX_FLAGS_RELEASE" to "-O3 -DNDEBUG -msse3",
            "SDL_GPU" to "OFF",
            "SDL_RENDER_D3D12" to "OFF",
        ),
    )
}
