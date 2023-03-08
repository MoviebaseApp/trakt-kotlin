[Website](https://www.trakt.tv) |
[Support](https://support.trakt.tv/support/home) |
[Documentation](https://trakt.docs.apiary.io/)

<a href="https://www.trakt.tv"><img alt="Trakt" src="doc/images/trakt-wide-red-black.svg" width="400"></a>

***Track your TV shows and movies with Trakt.***

[![Maven Central](https://img.shields.io/maven-central/v/app.moviebase/trakt-api?label=Maven%20Central)](https://search.maven.org/artifact/app.moviebase/trakt-api)
![Github Actions](https://github.com/MoviebaseApp/trakt-api/actions/workflows/build.yml/badge.svg)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8-green?style=flat)](https://gradle.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

<hr>

# Trakt API

Trakt API is a **Kotlin Multiplatform** library for tracking your series and movies.

* Written in Kotlin native with ktor from the ground up.
* Support for Android, iOS, desktop, and web applications.
* High customizable HttpClient configuration
* Fully supported by [Moviebase for Android](https://play.google.com/store/apps/details?id=com.moviebase)


## Adding to your project

The library is available on Maven Central.

### Gradle

Add the Maven Central repository if it is not already there.

```kotlin
repositories {
    mavenCentral()
}
```

To use the library in a single-platform project, add a dependency.

```kotlin
dependencies {
    implementation("app.moviebase:trakt-api:0.2.0")
}
```

In Kotlin Multiplatform projects, add the dependency to your commonMain source-set dependencies.

```kotlin
commonMain {
    dependencies {
        implementation("app.moviebase:trakt-api:0.2.0")
    }
}
```

### Maven

Add a dependency to the `<dependencies>` element.

```xml
<dependency>
    <groupId>app.moviebase</groupId>
    <artifactId>trakt-api</artifactId>
    <version>0.2.0</version>
</dependency>
```


## Usage
Most of the library follows the possibilities and naming at the official [Trakt documentation](https://trakt.docs.apiary.io/).

### Get information
For getting basic information about a shows or other media content.

```kotlin
val trakt = Trakt("clientId")
val traktShow = trakt.shows.getSummary(traktSlug = "vikings")
```

### Search
Search for TV shows by a query.

```kotlin
val trakt = Trakt("clientId")
val showPageResult = trakt.search.search(
    searchType = TraktSearchType.TMDB,
    id = "63639",
    mediaType = TraktMediaType.SHOW
)
```

<br>

<hr>

*This library uses the Trakt but is not endorsed or certified by Trakt.*
