# card-stack-for-compose

[![Maven Central](https://img.shields.io/maven-central/v/io.github.plz-no-anr/card-stack.svg)](https://central.sonatype.com/artifact/io.github.plz-no-anr/card-stack)
[![API](https://img.shields.io/badge/API-23%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=23)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)



### 1. Add the mavenCentral() on project level(root level) build.gradle file:
``` gradle
allprojects {
    repositories {
        mavenCentral()
    }
}
```

### 2. Add dependency on module level build.gradle file:

#### build.gradle
``` groovy
dependencies {
    implementation 'io.github.plz-no-anr:card-stack:latestVersion'
}
```

#### build.gradle.kts
``` kotlin dsl
dependencies {
    implementation("io.github.plz-no-anr:card-stack:$latestVersion")
}
```
