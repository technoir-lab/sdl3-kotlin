# Repository Guidelines

## Project Structure

- `sdl3/`: Kotlin/Native C-interop bindings for SDL3 in the `io.technoirlab.sdl3` package, published as `io.technoirlab.sdl3:sdl3`.
  The module has no handwritten API; the `io.technoirlab.cmake-import` plugin builds SDL from source for every
  Kotlin/Native target and generates the bindings from the installed headers.
- `sdl3/src/commonTest/kotlin`: tests that run against the statically linked SDL.
- `thirdparty/SDL`: SDL source, pinned as a shallow Git submodule to an upstream `release-*` tag.

## Build, Test, and Development Commands

Run commands from the repository root after `git submodule update --init --recursive`. Use the Gradle wrapper; the daemon
and CI use JDK 25. Building SDL requires CMake 3.29 or newer on `PATH`; Apple targets require macOS with Xcode.

- `make build`: build SDL and the bindings for every target the host supports and run checks.
- `make check`: run standard checks; builds SDL only for targets with test tasks, skipping the Android, Apple device, and
  `linuxArm64` targets.
- `make test`: run multiplatform tests on available targets.
- `make format`: apply KtLint formatting and sort dependencies.
- `make publish-local`: publish artifacts to Maven Local for consumer testing.

Pass additional options with `make check GRADLE_ARGS="--info"`.

- Run Make targets that invoke Gradle and direct `./gradlew` commands outside the filesystem sandbox so Gradle can access its cache.
- `./gradlew :sdl3:macosArm64Test`: build SDL for one target and run its tests; replace the target as needed.
- CI builds on macOS 26, Ubuntu 26.04, and Windows Server 2025. Apple targets build only on macOS, so releases publish from macOS.

## SDL Configuration

- Configure SDL through `cmakeImport.defines` and the per-target `CMakeGenerateTask` overrides in `sdl3/build.gradle.kts`.
- Keep the target list in `sdl3/build.gradle.kts` and `README.md` in sync.
- To update SDL, check out the new upstream tag in the submodule and update the SDL version in `README.md` in the same change:
  `git -C thirdparty/SDL fetch --depth 1 origin tag release-X.Y.Z && git -C thirdparty/SDL checkout release-X.Y.Z`.
  Review SDL's CMake option changes against the build configuration.

## Style & Naming

- Follow `.editorconfig` and IntelliJ ktlint style.
- Use UTF-8, LF endings, four-space indentation, and a 140-character line limit.
- Use two-space indentation for YAML.
- Allow trailing commas; avoid wildcard imports and trailing whitespace.
- Use `PascalCase` for types and `camelCase` for functions/properties.

## Testing

- Add shared tests to `sdl3/src/commonTest/kotlin`; use platform test source sets only for platform-specific behavior.
- Name test classes after subjects, e.g. `SDLInitTest`.
- Run `make build` before submitting changes that affect the SDL build or bindings.

## Commits and Pull Requests

- Use descriptive branch names without AI harness prefixes (such as `codex/`, `claude/`, `cursor/`, or `junie/`).
- Keep commits focused and use short, imperative commit subjects.
- Do not add a `Co-Authored-By` trailer.
- PR descriptions should explain the problem, the changes made, and the resulting behavior. Include compatibility impacts, remaining limitations, and links to related issues when relevant. Do not include checks performed, validation commands, or validation results.
- Describe affected targets.
- Label created pull requests `breaking change` when they drop a target or otherwise break compatibility with the latest published release.
