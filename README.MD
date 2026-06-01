KosherKotlin Zmanim API
=====================

The KosherKotlin Zmanim library is a Kotlin Multiplatform API for a specialized calendar that can calculate different astronomical
times including sunrise and sunset (default uses NOAA's algorithm) and Jewish _zmanim_ or religious times for prayers and other
Jewish religious duties.

It is based on Eliyahu Hershfeld's [KosherJava](https://github.com/KosherJava/zmanim).

For non religious astronomical / solar calculations use the [AstronomicalCalendar](./composeApp/src/commonMain/sternbach/software/kosherkotlin/AstronomicalCalendar.kt).

The `ZmanimCalendar` class contains the most common zmanim or religious time calculations. 
For a much more extensive list of _zmanim_ use the `ComplexZmanimCalendar`.
This class contains the main functionality of the Zmanim library.

For a basic set of instructions on the use of the API, see [How to Use the Zmanim API](https://kosherjava.com/zmanim-project/how-to-use-the-zmanim-api/), [zmanim code samples](https://kosherjava.com/tag/code-sample/) and the [KosherJava FAQ](https://kosherjava.com/tag/faq/). See the <a href="https://kosherjava.com">KosherJava Zmanim site</a> for additional information.

# Get Started

## Dependency

The library is published to Maven Central. Add it to your project:

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
}

dependencies {
    implementation("io.github.elfifo4:kosherkotlin:1.0.0")
}
```

The library supports Android, JVM (Desktop), iOS (arm64, x64, simulatorArm64), and JavaScript.

## Building from source

This library is currently a Compose Multiplatform app with a package for the library and a package for the app. The app is a demo of the library, and is not required to use the library.

### Before running!
- check your system with [KDoctor](https://github.com/Kotlin/kdoctor)
- install JDK 17 on your machine
- add `local.properties` file to the project root and set a path to Android SDK there

#### Android
To run the application on android device/emulator:
- open project in Android Studio and run imported android run configuration

To build the application bundle:
- run `./gradlew :composeApp:assembleDebug`
- find `.apk` file in `composeApp/build/outputs/apk/debug/composeApp-debug.apk`

#### Desktop
Run the desktop application: `./gradlew :composeApp:run`

#### iOS
To run the application on iPhone device/simulator:
- Open `iosApp/iosApp.xcproject` in Xcode and run standard configuration
- Or use [Kotlin Multiplatform Mobile plugin](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile) for Android Studio

#### Browser
Run the browser application: `./gradlew :composeApp:jsBrowserDevelopmentRun`

## Usage

The library was designed to be as idiomatic as possible, both in its implementation and its API surface. Every client-facing zero-argument function is accessible as a computed property (e.g. `ComplexZmanimCalender().alos72`).

Every zman has a lazily computed generic property called `value`.

`Zman.DateBased` objects have a `momentOfOccurrence` property which lazily computes the `Instant` of the zman's occurrence. 

`Zman.ValueBased` objects have a `duration` property which lazily computes the `Duration` of how long the zman lasts.

A lazy list of all zmanim can be accessed using the `allZmanim` property of `ComplexZmanimCalendar` or `ZmanimCalendar`.

Every `Zman` object has a `definition` property which contains the metadata of the rules used to calculate the zman.

```kotlin
val location = GeoLocation("New York", 40.7, -74.0, 0.0, TimeZone.of("America/New_York"))
val calendar = ComplexZmanimCalendar(location)
println(calendar.allZmanim.filter { it.definition.type == ZmanType.ALOS }.map { it.value })
println(calendar.sunrise)
println(calendar.alos72.momentOfOccurrence)
println(calendar.shaahZmanis72Minutes.duration)
```


Hosted documentation coming soon!

# Future
- [x] ~~There are plans to port the library to `kotlinx-datetime` so that it is multi-platform friendly. This is a major overhaul - PRs are welcomed.~~
    - [ ] ZmanimFormatter needs to be re-written once kotlinx-datetime implements formatting
- [x] ~~Library distribution needs to be improved (e.g. publishing to mavenCentral, etc.)~~
    - Published to Maven Central as `io.github.elfifo4:kosherkotlin`
- [ ] There are still a significant amount of javadocs/kdocs that were written for specific Java getters, setters, or properties, which were combined into a public property, and all of the javadocs/kdocs were copied to the property. They must be combined. There is often overlap or complete duplication between doc strings, but sometimes there are notes only relevant to the setter or getter.
- [ ] Unit tests need to be ported from upstream. They are (slowly) being translated into Java from the Python port.

# Publishing a New Release

This section is for maintainers.

### Prerequisites

`~/.gradle/gradle.properties` must contain:

```properties
mavenCentralUsername=<token from central.sonatype.com → View Account → Generate User Token>
mavenCentralPassword=<token password>
signingInMemoryKey=<GPG private key as a single line with \n separators>
signingInMemoryKeyPassword=<GPG key passphrase>
```

To convert your GPG private key to the required single-line format:

```bash
awk '{printf "%s\\n", $0}' ~/private-key.asc
```

### Steps

1. Update `version` in [`library/build.gradle.kts`](library/build.gradle.kts)
2. Commit and tag the release:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
3. Publish:
   ```bash
   ./gradlew :library:publishAndReleaseToMavenCentral --no-configuration-cache
   ```
4. Verify the deployment at [central.sonatype.com/publishing/deployments](https://central.sonatype.com/publishing/deployments)
5. After 15–30 minutes verify the artifact is live:
   ```
   https://repo1.maven.org/maven2/io/github/elfifo4/kosherkotlin/
   ```

### Testing before release

```bash
# Verify JARs and POM are generated correctly
./gradlew :library:publishToMavenLocal

# Inspect output
find ~/.m2/repository/io/github/elfifo4 -name "*.pom" | sort
```

# Projects using this library
- [BeautifulZmanim](https://github.com/Sternbach-Software/BeautifulZmanim): a multi-platform zmanim app for desktop, web, and mobile. Written using Jetpack Compose.

# License & Attribution
The library is released under the [LGPL 2.1 license](https://kosherjava.com/2011/05/09/kosherjava-zmanim-api-released-under-the-lgpl-license/).
If you made a publicly-available program that uses this library, I want to know! Just send me a heads-up email at "software at sternbach.org".

# Disclaimer
While I did my best to get accurate results, please double check before relying on these zmanim for *halacha lemaaseh*.
