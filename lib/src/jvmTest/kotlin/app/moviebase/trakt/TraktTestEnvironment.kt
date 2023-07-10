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
    traktApiKey: String? = null,
    authStore: TraktAuthStore? = null,
): Trakt {
    val configuration = defaultTraktConfiguration(traktApiKey, authStore)
    return Trakt(configuration)
}

fun defaultTraktConfiguration(
    traktApiKey: String? = null,
    authStore: TraktAuthStore? = null,
): TraktClientConfig.() -> Unit = {
    this.traktApiKey = traktApiKey ?: properties.getProperty("TRAKT_CLIENT_ID")
    requireNotNull(this.traktApiKey)

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
