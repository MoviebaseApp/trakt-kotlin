package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktEpisodeSummary
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.remote.endPoint
import app.moviebase.trakt.remote.parameterExtended
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TraktEpisodesApi(private val client: HttpClient) {

    suspend fun getSummary(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktEpisodeSummary = client.get {
        endPointEpisode(traktSlug, seasonNumber, episodeNumber)
        parameterExtended()
    }.body()

    suspend fun getRating(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktRating = client.get {
        endPointEpisode(traktSlug, seasonNumber, episodeNumber, "ratings")
    }.body()


    private fun HttpRequestBuilder.endPointEpisode(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        vararg paths: String
    ) {
        endPoint(
            "shows",
            traktSlug,
            "seasons",
            seasonNumber.toString(),
            "episodes",
            episodeNumber.toString(),
            *paths
        )
    }
}
