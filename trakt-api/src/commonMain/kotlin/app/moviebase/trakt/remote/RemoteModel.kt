package app.moviebase.trakt.remote

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json

internal fun buildHttpClient(logLevel: TraktLogLevel = TraktLogLevel.NONE, interceptor: RequestInterceptor): HttpClient {
    val json = buildJson()

    val httpClient = HttpClient {
        install(Logging) {
            logger = Logger.DEFAULT
            level = logLevel.ktorLogLevel
        }

        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 60_000
            connectTimeoutMillis = 60_000
            socketTimeoutMillis = 60_000
        }

    }
    httpClient.interceptRequest(interceptor = interceptor)
    return httpClient
}

private val TraktLogLevel.ktorLogLevel
    get() = when (this) {
        TraktLogLevel.ALL -> LogLevel.ALL
        TraktLogLevel.HEADERS -> LogLevel.HEADERS
        TraktLogLevel.BODY -> LogLevel.BODY
        TraktLogLevel.INFO -> LogLevel.INFO
        TraktLogLevel.NONE -> LogLevel.NONE
    }

internal fun buildJson(): Json = Json {
    encodeDefaults = false
    ignoreUnknownKeys = true
    isLenient = true
    allowSpecialFloatingPointValues = true
    prettyPrint = false
}
