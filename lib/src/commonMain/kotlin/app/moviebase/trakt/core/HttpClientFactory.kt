package app.moviebase.trakt.core

import app.moviebase.trakt.TraktBearerTokens
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
import io.ktor.client.plugins.auth.providers.BearerTokens
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

    private val retryClientHttpCodes by lazy {
        listOf(
            HttpStatusCode.RequestTimeout,
            HttpStatusCode.TooEarly,
            HttpStatusCode.TooManyRequests,
        ).map { it.value }.toSet()
    }

    fun create(
        config: TraktClientConfig,
        useAuthentication: Boolean = false,
    ): HttpClient {
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
            if (useAuthentication) {
                install(Auth) {
                    bearer {
                        loadTokens {
                            config.traktAuthCredentials?.loadTokensProvider?.invoke()?.toKtor()
                        }

                        refreshTokens {
                            config.traktAuthCredentials?.refreshTokensProvider?.invoke()?.toKtor()
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
                maxRetries = config.maxRetries
                exponentialDelay()

                retryIf { _, response ->
                    response.status.value.let { it in 500..599 || retryClientHttpCodes.contains(it) }
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

    private fun TraktBearerTokens.toKtor() = BearerTokens(accessToken, refreshToken)
}
