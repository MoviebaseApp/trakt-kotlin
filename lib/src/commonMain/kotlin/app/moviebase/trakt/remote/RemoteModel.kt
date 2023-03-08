package app.moviebase.trakt.remote

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal fun buildHttpClient(logLevel: TraktLogLevel = TraktLogLevel.NONE, interceptor: RequestInterceptor): HttpClient {
    val json = buildJson()

    val httpClient = HttpClient {
        Logging {
            logger = Logger.DEFAULT
            level = logLevel.ktorLogLevel
        }

        install(ContentNegotiation) {
            json(json)
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
