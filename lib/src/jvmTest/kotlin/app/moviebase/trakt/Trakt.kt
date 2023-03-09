package app.moviebase.trakt

import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

val properties by lazy {
    Properties().apply {
        val parent = Paths.get(System.getProperty("user.dir")).parent
        FileInputStream("$parent/local.properties").use {
            load(it)
        }
    }
}

fun buildTrakt(
    apiKey: String? = null,
    storage: TraktAccountStorage? = null,
): Trakt {
    return Trakt(defaultTmdbConfiguration(apiKey, storage))
}

fun defaultTmdbConfiguration(
    apiKey: String? = null,
    storage: TraktAccountStorage? = null,
): TraktClientConfig.() -> Unit = {
    traktApiKey = apiKey ?: properties.getProperty("TRAKT_CLIENT_ID")
    userAuthentication {
        loadBearerTokens { storage?.bearerTokens }
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
