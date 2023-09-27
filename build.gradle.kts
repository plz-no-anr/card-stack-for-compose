import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.android.library) apply false
    alias(libs.plugins.io.github.gradle.nexus.publish)
}

val properties = gradleLocalProperties(rootDir)

val userMap = mapOf(
    "ossrhUsername" to properties.getProperty("ossrhUsername"),
    "ossrhPassword" to properties.getProperty("ossrhPassword"),
    "sonatypeStagingProfileId" to properties.getProperty("sonatypeStagingProfileId"),
)

nexusPublishing {
    repositories {
        sonatype {
            stagingProfileId.set(userMap["sonatypeStagingProfileId"])
            username.set(userMap["ossrhUsername"])
            password.set(userMap["ossrhPassword"])
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
        }
    }
}
