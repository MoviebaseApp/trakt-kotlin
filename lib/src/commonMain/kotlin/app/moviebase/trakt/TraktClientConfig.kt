package app.moviebase.trakt

import app.moviebase.trakt.core.TraktDsl
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.plugins.logging.Logging

@TraktDsl
class TraktClientConfig {

    var traktApiKey: String? = null
    var clientSecret: String? = null

    internal var traktAuthCredentials: TraktAuthCredentials? = null

    var expectSuccess: Boolean = true
    var useCache: Boolean = false
    var useTimeout: Boolean = false
    var maxRetries: Int = 3

    internal var httpClientConfigBlock: (HttpClientConfig<*>.() -> Unit)? = null
    internal var httpClientBuilder: (() -> HttpClient)? = null
    internal var httpClientLoggingBlock: (Logging.Config.() -> Unit)? = null

    fun userAuthentication(block: TraktAuthCredentials.() -> Unit) {
        traktAuthCredentials = TraktAuthCredentials().apply(block)
    }

    fun logging(block: Logging.Config.() -> Unit) {
        httpClientLoggingBlock = block
    }

    /**
     * Set custom HttpClient configuration for the default HttpClient.
     */
    fun httpClient(block: HttpClientConfig<*>.() -> Unit) {
        this.httpClientConfigBlock = block
    }

    /**
     * Creates an custom [HttpClient] with the specified [HttpClientEngineFactory] and optional [block] configuration.
     * Note that the Trakt config will be added afterwards.
     */
    fun <T : HttpClientEngineConfig> httpClient(
        engineFactory: HttpClientEngineFactory<T>,
        block: HttpClientConfig<T>.() -> Unit = {},
    ) {
        httpClientBuilder = {
            HttpClient(engineFactory, block)
        }
    }

    companion object {

        internal fun withKey(tmdbApiKey: String) = TraktClientConfig().apply {
            this.traktApiKey = tmdbApiKey
        }
    }
}

@TraktDsl
class TraktAuthCredentials {

    internal var refreshTokensProvider: (suspend () -> TraktBearerTokens?)? = null
    internal var loadTokensProvider: (suspend () -> TraktBearerTokens?)? = null

    fun refreshTokens(provider: suspend() -> TraktBearerTokens?) {
        refreshTokensProvider = provider
    }

    fun loadTokens(provider: suspend() -> TraktBearerTokens?) {
        loadTokensProvider = provider
    }
}

data class TraktBearerTokens(
    val accessToken: String,
    val refreshToken: String,
)
