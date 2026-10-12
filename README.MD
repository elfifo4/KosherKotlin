# KosherKotlin

[![Maven Central](https://img.shields.io/maven-central/v/io.github.elfifo4/kosherkotlin)](https://central.sonatype.com/artifact/io.github.elfifo4/kosherkotlin)
[![Unit tests](https://github.com/elfifo4/KosherKotlin/actions/workflows/pull_request_worklow.yml/badge.svg?branch=master)](https://github.com/elfifo4/KosherKotlin/actions/workflows/pull_request_worklow.yml)
[![License: LGPL 2.1](https://img.shields.io/badge/license-LGPL%202.1-blue.svg)](LICENSE)

A Kotlin Multiplatform library for Jewish calendar and _zmanim_ calculations: sunrise, sunset and
other astronomical times (NOAA algorithm by default), halachic times for prayer and other religious
duties, Hebrew dates, holidays, the weekly _parsha_ and _Daf Yomi_.

It is a Kotlin port of Eliyahu Hershfeld's [KosherJava Zmanim API](https://github.com/KosherJava/zmanim),
built on `kotlinx-datetime` so the same code runs on Android, iOS, desktop JVM and the browser.
See [Project history and attribution](#project-history-and-attribution) for who wrote what.

## Supported targets

| Target | Published artifact | Notes |
| --- | --- | --- |
| Android | `kosherkotlin-android` | `minSdk` 24 |
| JVM (desktop) | `kosherkotlin-desktop` | JDK 17 toolchain |
| iOS | `kosherkotlin-iosarm64`, `-iossimulatorarm64`, `-iosx64` | |
| JavaScript (browser) | `kosherkotlin-js` | Kotlin/JS IR |

## Installation

The library is on Maven Central. In a Kotlin Multiplatform project, depend on the root artifact and
Gradle resolves the right variant for each target:

```kotlin
// build.gradle.kts
repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("io.github.elfifo4:kosherkotlin:1.0.1")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.8.0")
        }
    }
}
```

In an Android-only or JVM-only project, the same coordinates go in the regular `dependencies` block.

`kotlinx-datetime` types such as `TimeZone`, `LocalDate` and `Instant` appear in the public API, but
the published artifact does not yet export that dependency to consumers at compile time. Until that
is fixed, declare `kotlinx-datetime` yourself as shown above.

## Usage

### Zmanim for a location

```kotlin
import kotlinx.datetime.TimeZone
import sternbach.software.kosherkotlin.ComplexZmanimCalendar
import sternbach.software.kosherkotlin.metadata.ZmanType
import sternbach.software.kosherkotlin.util.GeoLocation

val location = GeoLocation(
    "Lakewood, NJ",
    40.096,                           // latitude
    -74.222,                          // longitude
    29.02,                            // elevation in meters
    TimeZone.of("America/New_York")
)
val calendar = ComplexZmanimCalendar(location)  // defaults to today's date in that time zone

println(calendar.sunrise)                             // Instant?
println(calendar.alos72.momentOfOccurrence)           // Instant?
println(calendar.shaahZmanis72Minutes.duration)       // Duration

// Every opinion for a given kind of zman, with the rule each one is computed by
calendar.allZmanim
    .filter { it.definition.type == ZmanType.ALOS }
    .forEach { println("${it.definition} -> ${it.value}") }
```

`ZmanimCalendar` holds the most commonly used zmanim. `ComplexZmanimCalendar` extends it with a much
larger set of opinions. For purely astronomical calculations, use `AstronomicalCalendar`.

To calculate for another day, set `calendar.localDateTime`.

### Hebrew calendar

```kotlin
import kotlinx.datetime.LocalDate
import sternbach.software.kosherkotlin.hebrewcalendar.HebrewDateFormatter
import sternbach.software.kosherkotlin.hebrewcalendar.JewishCalendar

val day = JewishCalendar(LocalDate(2026, 4, 11))
val formatter = HebrewDateFormatter()

println(formatter.format(day))        // the Hebrew date
println(formatter.formatParsha(day))  // "Shmini"
println(day.parshah)               // the JewishCalendar.Parsha enum value; NONE on a weekday
println(day.isRoshChodesh)
println(day.dafYomiBavli)
```

### API conventions

The port aims for an idiomatic Kotlin surface rather than a line-by-line translation:

- Zero-argument getters from the Java API are properties (`calendar.alos72`, not `getAlos72()`).
- A zman is a `Zman` object, not a bare date. `Zman.DateBased` exposes `momentOfOccurrence`
  (an `Instant?`, `null` where the sun does not reach the required position, as in polar regions).
  `Zman.ValueBased` exposes `duration`. Both compute lazily.
- Every `Zman` carries a `definition`: structured metadata describing its type, the calculation
  method and the authority it follows. `allZmanim` returns all of them as a list, so an app can
  filter, group and label zmanim without hard-coding each one.

The calculations themselves follow KosherJava, so its documentation applies:
[How to Use the Zmanim API](https://kosherjava.com/zmanim-project/how-to-use-the-zmanim-api/),
[code samples](https://kosherjava.com/tag/code-sample/) and the
[FAQ](https://kosherjava.com/tag/faq/).

## Project structure

```
library/      the published library; all code is in commonMain, with no platform-specific sources
  src/commonMain/kotlin/sternbach/software/kosherkotlin/
    AstronomicalCalendar.kt, ZmanimCalendar.kt, ComplexZmanimCalendar.kt, Zman.kt
    hebrewcalendar/   JewishDate, JewishCalendar, HebrewDateFormatter, Daf Yomi, TefilaRules
    metadata/         ZmanDefinition, ZmanType, ZmanAuthority, calculation methods
    util/             GeoLocation, NOAACalculator, SunTimesCalculator
  src/commonTest/     tests that run on every target
  src/desktopTest/    JVM tests, including regression tests against KosherJava
composeApp/   Compose Multiplatform demo app (Android, desktop, iOS, web); not needed to use the library
iosApp/       Xcode project that hosts the iOS demo
```

## Testing

```bash
./gradlew :library:desktopTest
```

The JVM test suite depends on the original `com.kosherjava:zmanim` artifact and compares this port's
output against it across dates and locations, so behavioural drift from the reference implementation
shows up as a test failure. Calendar logic that has no JVM dependency is also covered in
`commonTest`. CI runs the JVM suite on every push and pull request.

## Building the demo app

Requirements: JDK 17, and a `local.properties` file at the project root pointing to the Android SDK.
[KDoctor](https://github.com/Kotlin/kdoctor) can check the rest of the environment.

| Platform | Command |
| --- | --- |
| Android | `./gradlew :composeApp:assembleDebug` |
| Desktop | `./gradlew :composeApp:run` |
| Browser | `./gradlew :composeApp:jsBrowserDevelopmentRun` |
| iOS | open `iosApp/iosApp.xcodeproj` in Xcode and run |

## Project history and attribution

This repository is one link in a chain of work by several people:

- **[KosherJava Zmanim API](https://github.com/KosherJava/zmanim)** by Eliyahu Hershfeld is the
  original library. The algorithms, the halachic research behind them and most of the
  documentation comments originate there.
- **[Sternbach-Software/KosherKotlin](https://github.com/Sternbach-Software/KosherKotlin)** by
  Sternbach Software is the Kotlin port: the translation to Kotlin, the `Zman` and metadata model, and
  the migration from `java.util`/`java.time` to `kotlinx-datetime` that made the code multiplatform.
  The `sternbach.software.kosherkotlin` package name is kept from that project.
- **This fork**, maintained by [Elad Finish](https://github.com/elfifo4), packages the port as a
  consumable library. Its changes are: extracting the library from the demo app into a standalone
  `:library` Gradle module, publishing it to Maven Central for Android, JVM, iOS and JS, upgrading
  the toolchain and dependencies, a fix to the parsha calculation with regression tests, and
  repairing the web target of the demo app. The full list is in the
  [commit history](https://github.com/elfifo4/KosherKotlin/commits/master).

## Known limitations

- `ZmanimFormatter` from KosherJava has not been ported.
- Some KDoc comments were merged from separate Java getters and setters into one Kotlin property
  and still contain duplicated or getter-specific wording, including references to
  `com.kosherjava` class names.
- Only part of the upstream test suite has been ported.

## Releasing

Maintainer instructions are in [RELEASING.md](RELEASING.md).

## License

Released under the [GNU Lesser General Public License v2.1](LICENSE), the same license as
[KosherJava](https://kosherjava.com/2011/05/09/kosherjava-zmanim-api-released-under-the-lgpl-license/).

## Disclaimer

Every effort is made to produce accurate results, but please double-check before relying on these
zmanim for _halacha lemaaseh_.
