package app.moviebase.trakt

import app.moviebase.trakt.api.TraktCheckinApi
import app.moviebase.trakt.api.TraktSeasonsApi
import app.moviebase.trakt.api.TraktSearchApi
import app.moviebase.trakt.api.TraktShowsApi
import app.moviebase.trakt.remote.TraktHttpClientFactory
import app.moviebase.trakt.remote.TraktLogLevel
import io.ktor.client.*

class Trakt(
    traktClientId: String,
    logLevel: TraktLogLevel = TraktLogLevel.NONE
) {

    private val client: HttpClient = TraktHttpClientFactory.create(traktClientId, logLevel)

    val shows = TraktShowsApi(client)
    val checkin = TraktCheckinApi(client)
    val seasons = TraktSeasonsApi(client)
    val search = TraktSearchApi(client)
}

