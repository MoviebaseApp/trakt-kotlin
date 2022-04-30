package app.moviebase.trakt.remote

import app.moviebase.trakt.TraktHeader
import app.moviebase.trakt.TraktUrlParameter
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.request.*

internal object TraktHttpClientFactory {

    fun create(traktClientId: String, logLevel: TraktLogLevel) = buildHttpClient(logLevel) {
        it.header(TraktHeader.API_KEY, traktClientId)
        it.header(TraktHeader.API_VERSION, TraktWebConfig.VERSION)
    }
}
