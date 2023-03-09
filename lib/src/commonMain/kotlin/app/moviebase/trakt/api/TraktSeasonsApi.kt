package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get

class TraktSeasonsApi(private val client: HttpClient) {

    suspend fun getEpisodes(traktSlug: String, seasonNumber: Int): List<TraktEpisode> = client.get {
        endPointSeason(traktSlug, seasonNumber)
        parameterExtended()
    }.body()

    suspend fun getRating(traktSlug: String, seasonNumber: Int): TraktRating = client.get {
        endPointSeason(traktSlug, seasonNumber, "ratings")
    }.body()

    private fun HttpRequestBuilder.endPointSeason(traktSlug: String, seasonNumber: Int, vararg paths: String) {
        endPoint("shows", traktSlug, "seasons", seasonNumber.toString(), *paths)
    }
}
