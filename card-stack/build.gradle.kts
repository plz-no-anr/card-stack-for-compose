import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    `version-catalog`
    `maven-publish`
    signing
}

android {
    namespace = "com.plznoanr.card_stack"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)
    api(libs.compose.material)
    implementation(libs.material3)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}

val properties = gradleLocalProperties(rootDir)
val signingMap = mapOf(
    "signing.keyId" to properties.getProperty("signing.keyId"),
    "signing.password" to properties.getProperty("signing.password"),
    "signing.key" to properties.getProperty("signing.key")
)

val androidSourceJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
}

android.publishing.singleVariant("release")

val publishGroup = "io.github.plz-no-anr"
val publishArtifact = "card-stack"
val publishVersion = "1.0.0"

group = publishGroup
version = publishVersion

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                if (project.plugins.hasPlugin("com.android.library")) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }
                artifact(androidSourceJar.get())
                groupId = publishGroup
                artifactId = publishArtifact
                version = publishVersion
                // Mostly self-explanatory metadata
                pom {
                    name.set("card-stack-for-compose")
                    description.set("Card Stack for Compose")
                    url.set("https://github.com/plz-no-anr/card-stack-for-compose")
                    licenses {
                        license {
                            name.set("MIT license")
                            url.set("https://opensource.org/license/mit")
                        }
                    }
                    developers {
                        developer {
                            id.set("plz-no-anr")
                            name.set("SanggunPark")
                            email.set("psgxxx@naver.com")
                        }
                    }
                    // Version control info
                    scm {
                        connection.set("scm:git:github.com/plz-no-anr/card-stack-for-compose.git")
                        developerConnection.set("scm:git:ssh://github.com/plz-no-anr/card-stack-for-compose.git")
                        url.set("https://github.com/plz-no-anr/card-stack-for-compose/tree/main")
                    }
                }

            }

        }
    }
}

signing {
    useInMemoryPgpKeys(
        signingMap["signing.keyId"],
        signingMap["signing.key"],
        signingMap["signing.password"]
    )
    sign(publishing.publications)
}