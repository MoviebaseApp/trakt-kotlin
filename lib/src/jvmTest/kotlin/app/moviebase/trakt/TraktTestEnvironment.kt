package app.moviebase.trakt

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

val properties by lazy {
    Properties().apply {
        val parent = Paths.get(System.getProperty("user.dir")).parent
        FileInputStream("$parent/local.properties").use {
            load(it)
        }
    }
}

fun createTraktCredentials() = TraktCredentials(
    accessToken = properties.getProperty("TRAKT_ACCESS_TOKEN"),
    refreshToken = properties.getProperty("TRAKT_REFRESH_TOKEN"),
)


fun buildTrakt(
    apiKey: String? = null,
    authStore: TraktAuthStore? = null,
): Trakt {
    return Trakt(defaultTmdbConfiguration(apiKey, authStore))
}

fun defaultTmdbConfiguration(
    apiKey: String? = null,
    authStore: TraktAuthStore? = null,
): TraktClientConfig.() -> Unit = {
    traktApiKey = apiKey ?: properties.getProperty("TRAKT_CLIENT_ID")
    userAuthentication {
        refreshTokens { authStore?.bearerTokens }
        loadTokens { authStore?.bearerTokens }
    }

    useCache = true
    useTimeout = true
    maxRetriesOnException = 3

    httpClient(OkHttp) {
        logging {
            logger = TestLogger()
            level = LogLevel.HEADERS
        }
    }
}

class TestLogger : Logger {

    override fun log(message: String) {
        println("HttpClient: $message")
    }
}
