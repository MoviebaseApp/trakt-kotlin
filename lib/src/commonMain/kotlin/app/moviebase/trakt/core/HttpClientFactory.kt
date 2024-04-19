package app.moviebase.trakt.core

import app.moviebase.trakt.TraktClientConfig
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.utils.unwrapCancellationException
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLProtocol
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.CancellationException

internal object HttpClientFactory {

    fun create(config: TraktClientConfig): HttpClient {
        val defaultConfig: HttpClientConfig<*>.() -> Unit = {
            val json = JsonFactory.create()

            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = TraktWebConfig.HOST
                }
            }

            install(ContentNegotiation) {
                json(json)
            }

            // see https://ktor.io/docs/auth.html
            val authCredentials = config.traktAuthCredentials
            if (authCredentials != null) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            // TODO: load cached tokens
                            authCredentials.loadTokensProvider()
                        }

                        refreshTokens {
                            // TODO:  add here the 401 handling
                            authCredentials.refreshTokensProvider()
                        }

                        sendWithoutRequest { request ->
                            request.url.host == TraktWebConfig.HOST
                        }
                    }
                }
            }

            // see https://ktor.io/docs/response-validation.html
            expectSuccess = config.expectSuccess

            // see https://ktor.io/docs/client-retry.html
            install(HttpRequestRetry) {
                exponentialDelay()

                retryIf(config.maxRetries) { _, httpResponse ->
                    when {
                        httpResponse.status.value in 500..599 -> true
                        httpResponse.status == HttpStatusCode.TooManyRequests -> true
                        else -> false
                    }
                }

                retryOnExceptionIf { _, cause ->
                    when {
                        cause.isTimeoutException() -> false
                        cause is CancellationException -> false
                        else -> true
                    }
                }
            }

            // see https://ktor.io/docs/client-caching.html
            if (config.useCache) {
                install(HttpCache)
            }

            if (config.useTimeout) {
                install(HttpTimeout) {
                    requestTimeoutMillis = 60_000
                    connectTimeoutMillis = 60_000
                    socketTimeoutMillis = 60_000
                }
            }

            config.httpClientLoggingBlock?.let {
                Logging(it)
            }

            // add custom configuration
            config.httpClientConfigBlock?.invoke(this)
        }

        return config.httpClientBuilder?.invoke()?.config(defaultConfig) ?: HttpClient(defaultConfig)
    }

    private fun Throwable.isTimeoutException(): Boolean {
        val exception = unwrapCancellationException()
        return exception is HttpRequestTimeoutException ||
            exception is ConnectTimeoutException ||
            exception is SocketTimeoutException
    }
}
