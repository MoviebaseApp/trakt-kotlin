package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktShow
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktShowsApi(private val client: HttpClient) {

    suspend fun getSummary(traktSlug: String): TraktShow = client.get {
        endPointShows(traktSlug)
        parameterExtended()
    }.body()

    suspend fun getRating(traktSlug: String): TraktRating = client.get {
        endPointShows(traktSlug, "ratings")
    }.body()

    private fun HttpRequestBuilder.endPointShows(traktSlug: String, vararg paths: String) {
        endPoint("shows", traktSlug, *paths)
    }
}
