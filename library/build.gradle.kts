plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    id("maven-publish")
    id("signing")
}

group = "io.github.elad-finish"
version = "1.0.0"

kotlin {
    applyDefaultHierarchyTemplate()

    android {
        namespace = "sternbach.software.kosherkotlin.library"
        compileSdk = 37
        minSdk = 24
    }

    jvmToolchain(17)

    jvm("desktop")

    js { browser() }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "KosherKotlin"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.napier)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        val desktopTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("com.kosherjava:zmanim:2.5.0")
            }
        }
    }
}

publishing {
    publications.withType<MavenPublication> {
        artifactId = artifactId.replace("library", "kosherkotlin")
        pom {
            name.set("KosherKotlin")
            description.set("Kotlin Multiplatform port of KosherJava — Jewish calendar & zmanim calculations")
            url.set("https://github.com/elad-finish/KosherKotlin")
            licenses {
                license {
                    name.set("GNU Lesser General Public License v2.1")
                    url.set("https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html")
                }
            }
            developers {
                developer {
                    id.set("elad-finish")
                    name.set("Elad Finish")
                    email.set("elfifo4@gmail.com")
                }
            }
            scm {
                connection.set("scm:git:github.com/elad-finish/KosherKotlin.git")
                developerConnection.set("scm:git:ssh://github.com/elad-finish/KosherKotlin.git")
                url.set("https://github.com/elad-finish/KosherKotlin")
            }
        }
    }
    repositories {
        maven {
            name = "sonatype"
            val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = project.findProperty("ossrhUsername") as String? ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword") as String? ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

val signingKey: String? by project
val signingPassword: String? by project

signing {
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}
