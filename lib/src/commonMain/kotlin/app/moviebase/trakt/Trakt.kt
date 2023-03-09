package app.moviebase.trakt

import app.moviebase.trakt.api.TraktCheckinApi
import app.moviebase.trakt.api.TraktEpisodesApi
import app.moviebase.trakt.api.TraktMoviesApi
import app.moviebase.trakt.api.TraktSearchApi
import app.moviebase.trakt.api.TraktSeasonsApi
import app.moviebase.trakt.api.TraktShowsApi
import app.moviebase.trakt.core.HttpClientFactory
import app.moviebase.trakt.core.TraktDsl
import app.moviebase.trakt.core.interceptRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.header

@TraktDsl
fun Trakt(block: TraktClientConfig.() -> Unit): Trakt {
    val config = TraktClientConfig().apply(block)
    return Trakt(config)
}

class Trakt internal constructor(private val config: TraktClientConfig) {

    constructor(tmdbApiKey: String) : this(TraktClientConfig.withKey(tmdbApiKey))

    private val client: HttpClient = HttpClientFactory.create(config).apply {
        interceptRequest {
            it.header(TraktHeader.API_KEY, config.traktApiKey)
            it.header(TraktHeader.API_VERSION, TraktWebConfig.VERSION)
        }
    }

    init {
        requireNotNull(config.traktApiKey) {
            "TMDB API key unavailable. Set the tmdbApiKey field in the class TmdbClientConfig when instantiate the TMDB client."
        }
    }

    val movies by buildApi(::TraktMoviesApi)
    val shows by buildApi(::TraktShowsApi)
    val seasons by buildApi(::TraktSeasonsApi)
    val episodes by buildApi(::TraktEpisodesApi)
    val checkin by buildApi(::TraktCheckinApi)
    val search by buildApi(::TraktSearchApi)

    private inline fun <T> buildApi(crossinline builder: (HttpClient) -> T) = lazy {
        builder(client)
    }
}
