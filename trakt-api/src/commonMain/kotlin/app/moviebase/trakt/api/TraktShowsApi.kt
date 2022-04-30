package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktShow
import app.moviebase.trakt.remote.endPoint
import app.moviebase.trakt.remote.parameterExtended
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TraktShowsApi(private val client: HttpClient) {

    suspend fun getSummary(traktSlug: String): TraktShow = client.get {
        endPointShows(traktSlug)
        parameterExtended()
    }.body()

    private fun HttpRequestBuilder.endPointShows(traktSlug: String, vararg paths: String) {
        endPoint("shows", traktSlug, *paths)
    }
}
