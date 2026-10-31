import org.jetbrains.kotlin.konan.target.HostManager

plugins {
    id("io.technoirlab.conventions.kotlin-multiplatform-application")
}

kotlinMultiplatformApplication {
    packageName = "io.technoirlab.sdl3.sample"
}

kotlin {
    androidNativeArm64()

    if (HostManager.hostIsLinux) {
        linuxArm64()
        linuxX64()
    }
    if (HostManager.hostIsMac) {
        iosArm64()
        iosSimulatorArm64()
        macosArm64()
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
