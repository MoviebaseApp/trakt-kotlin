package app.moviebase.trakt

import app.moviebase.trakt.api.*
import app.moviebase.trakt.remote.TraktHttpClientFactory
import app.moviebase.trakt.remote.TraktLogLevel
import io.ktor.client.*

class Trakt(
    traktClientId: String,
    logLevel: TraktLogLevel = TraktLogLevel.NONE
) {

    private val client: HttpClient = TraktHttpClientFactory.create(traktClientId, logLevel)

    val movies = TraktMoviesApi(client)
    val shows = TraktShowsApi(client)
    val seasons = TraktSeasonsApi(client)
    val episodes = TraktEpisodesApi(client)
    val checkin = TraktCheckinApi(client)
    val search = TraktSearchApi(client)
}

