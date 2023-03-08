package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.remote.endPoint
import app.moviebase.trakt.remote.parameterExtended
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

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
