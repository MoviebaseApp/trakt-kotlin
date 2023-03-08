package app.moviebase.trakt.remote

import app.moviebase.trakt.TraktHeader
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.request.header

internal object TraktHttpClientFactory {

    fun create(traktClientId: String) = buildHttpClient {
        it.header(TraktHeader.API_KEY, traktClientId)
        it.header(TraktHeader.API_VERSION, TraktWebConfig.VERSION)
    }
}
