import org.gradle.language.cpp.CppBinary
import org.jetbrains.kotlin.gradle.plugin.mpp.SharedLibrary
import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    packageName = "io.technoirlab.sdl3.sample"
}

kotlin {
    androidNativeArm64 {
        // Expose the shared libraries to the Android application in the android directory.
        binaries.withType<SharedLibrary>().configureEach {
            configurations.consumable("androidNativeArm64${name.replaceFirstChar(Char::uppercaseChar)}Elements") {
                attributes {
                    attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.NATIVE_RUNTIME))
                    attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, buildType.debuggable)
                }
                outgoing.artifact(outputFile) {
                    builtBy(linkTaskProvider)
                }
            }
        }
    }

    if (HostManager.hostIsLinux) {
        linuxArm64()
        linuxX64()
    }
    if (HostManager.hostIsMac) {
        iosArm64()
        iosSimulatorArm64()
        macosArm64()
        tvosArm64()
        tvosSimulatorArm64()
    }
    if (HostManager.hostIsMingw) {
        mingwX64()
    }

    compilerOptions {
        optIn.add("kotlinx.cinterop.ExperimentalForeignApi")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":sdl3"))
        }
    }
}
