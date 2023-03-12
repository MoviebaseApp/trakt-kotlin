[Website](https://www.trakt.tv) |
[Support](https://support.trakt.tv/support/home) |
[Documentation](https://trakt.docs.apiary.io/)

<a href="https://www.trakt.tv"><img alt="Trakt" src="doc/images/trakt-wide-red-black.svg" width="300"></a>

***Track and discover your TV shows and movies with Trakt.***

[![Maven Central](https://img.shields.io/maven-central/v/app.moviebase/trakt-api?label=Maven%20Central)](https://central.sonatype.com/artifact/app.moviebase/trakt-api/)
![Github Actions](https://github.com/MoviebaseApp/trakt-api/actions/workflows/build.yml/badge.svg)
[![Issues](https://img.shields.io/github/issues/MoviebaseApp/trakt-api)](https://github.com/MoviebaseApp/tmdb-api/issues)
[![Kotlin](https://img.shields.io/badge/kotlin-1.8.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-8-green?style=flat)](https://gradle.org)
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
[![GitHub Account](https://img.shields.io/static/v1?label=GitHub&message=chrisnkrueger&color=C51162)](https://github.com/chrisnkrueger)

<hr>

# Trakt API

Trakt API is a **Kotlin Multiplatform** library for tracking your series and movies.

* Written in Kotlin native with ktor from the ground up.
* Support for Android, iOS, desktop, and web applications.
* High customizable HttpClient configuration

Sample projects:

* [tivi](https://github.com/chrisbanes/tivi)
* [Moviebase](https://play.google.com/store/apps/details?id=com.moviebase)

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
    implementation("app.moviebase:trakt-api:0.3.0")
}
```

In Kotlin Multiplatform projects, add the dependency to your commonMain source-set dependencies.

```kotlin
commonMain {
    dependencies {
        implementation("app.moviebase:trakt-api:0.3.0")
    }
}
```

### Maven

Add a dependency to the `<dependencies>` element.

```xml
<dependency>
    <groupId>app.moviebase</groupId>
    <artifactId>trakt-api</artifactId>
    <version>0.3.0</version>
</dependency>
```


## Usage
Most of the library follows the possibilities and naming at the official [Trakt documentation](https://trakt.docs.apiary.io/).

### Configuration
Build up your Trakt instance to access the APIs. You can configure the entire HttpClient of ktor.

```kotlin
val trakt = Trakt {
    traktApiKey = "clientId"

    expectSuccess = false // if you want to disable exceptions
    useCache = true
    useTimeout = true
    maxRetriesOnException = 3 // retries when network calls throw an exception

    // add your own client
    httpClient(OkHttp) {
        engine {

        }
    }

    httpClient {
        // for custom HttpClient configuration
    }
}
```

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

## Contributing 🤝
Please feel free to [open an issue](https://github.com/MoviebaseApp/trakt-api/issues/new/choose) if you have any questions or suggestions. Or participate in the [discussion](https://github.com/MoviebaseApp/trakt-api/discussions). If you want to contribute, please read the [contribution guidelines](https://github.com/MoviebaseApp/trakt-api/blob/main/CONTRIBUTING.md) for more information.

<br>

<hr>

*This library uses the Trakt but is not endorsed or certified by Trakt.*
