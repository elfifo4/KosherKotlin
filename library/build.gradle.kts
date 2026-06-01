plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vanniktech.publish)
}

group = "io.github.elfifo4"
version = "1.0.1"

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
        @Suppress("unused")
        val desktopTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.kosherjava.zmanim)
            }
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates("io.github.elfifo4", "kosherkotlin", version.toString())

    pom {
        name = "KosherKotlin"
        description = "Kotlin Multiplatform port of KosherJava — Jewish calendar & zmanim calculations"
        url = "https://github.com/elfifo4/KosherKotlin"
        licenses {
            license {
                name = "GNU Lesser General Public License v2.1"
                url = "https://www.gnu.org/licenses/old-licenses/lgpl-2.1.html"
            }
        }
        developers {
            developer {
                id = "elad-finish"
                name = "Elad Finish"
                email = "elfifo4@gmail.com"
            }
        }
        scm {
            connection = "scm:git:github.com/elad-finish/KosherKotlin.git"
            developerConnection = "scm:git:ssh://github.com/elad-finish/KosherKotlin.git"
            url = "https://github.com/elfifo4/KosherKotlin"
        }
    }
}
