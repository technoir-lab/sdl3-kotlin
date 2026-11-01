import org.gradle.language.cpp.CppBinary

plugins {
    id("io.technoirlab.conventions.android-application")
}

androidApplication {
    applicationId = "io.technoirlab.sdl3.sample"
    packageName = "io.technoirlab.sdl3.sample.android"
}

android {
    // SDL's Java glue loads the sample's shared library and calls its SDL_main.
    sourceSets.getByName("main").java.directories += "../../thirdparty/SDL/android-project/app/src/main/java"
}

val nativeLibraries = configurations.dependencyScope("nativeLibraries")

dependencies {
    nativeLibraries(project(":sample"))
}

androidComponents.onVariants { variant ->
    val variantNativeLibraries = configurations.resolvable("${variant.name}NativeLibraries") {
        extendsFrom(nativeLibraries.get())
        attributes {
            attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.NATIVE_RUNTIME))
            attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, variant.debuggable)
        }
    }
    val copyNativeLibraries = tasks.register<CopyNativeLibraries>(variant.computeTaskName("copy", "NativeLibraries")) {
        libraries.from(variantNativeLibraries)
    }
    variant.sources.jniLibs?.addGeneratedSourceDirectory(copyNativeLibraries, CopyNativeLibraries::outputDirectory)
}

/**
 * Copies shared libraries to the arm64-v8a ABI directory, which matches the sample's androidNativeArm64 target.
 */
@DisableCachingByDefault(because = "Copying files is faster than caching them")
abstract class CopyNativeLibraries : DefaultTask() {
    @get:InputFiles
    @get:PathSensitive(PathSensitivity.NAME_ONLY)
    abstract val libraries: ConfigurableFileCollection

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    @get:Inject
    abstract val fileSystemOperations: FileSystemOperations

    @TaskAction
    fun copy() {
        fileSystemOperations.sync {
            from(libraries)
            into(outputDirectory.dir("arm64-v8a"))
        }
    }
}
