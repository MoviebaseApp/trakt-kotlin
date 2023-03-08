package app.moviebase.trakt

import app.moviebase.trakt.api.TraktCheckinApi
import app.moviebase.trakt.api.TraktEpisodesApi
import app.moviebase.trakt.api.TraktMoviesApi
import app.moviebase.trakt.api.TraktSearchApi
import app.moviebase.trakt.api.TraktSeasonsApi
import app.moviebase.trakt.api.TraktShowsApi
import app.moviebase.trakt.remote.TraktHttpClientFactory
import app.moviebase.trakt.remote.TraktLogLevel
import io.ktor.client.HttpClient

class Trakt(
    traktClientId: String,
    logLevel: TraktLogLevel = TraktLogLevel.NONE,
) {

    private val client: HttpClient = TraktHttpClientFactory.create(traktClientId, logLevel)

    val movies = TraktMoviesApi(client)
    val shows = TraktShowsApi(client)
    val seasons = TraktSeasonsApi(client)
    val episodes = TraktEpisodesApi(client)
    val checkin = TraktCheckinApi(client)
    val search = TraktSearchApi(client)
}
