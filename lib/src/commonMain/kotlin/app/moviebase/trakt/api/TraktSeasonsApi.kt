package app.moviebase.trakt.api

import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient

class TraktSeasonsApi(private val client: HttpClient) {

    suspend fun getEpisodes(traktSlug: String, seasonNumber: Int): List<TraktEpisode> =
        client.getByPaths(*pathSeasons(traktSlug, seasonNumber)) {
            parameterExtended()
        }

    suspend fun getRating(
        traktSlug: String,
        seasonNumber: Int,
    ): TraktRating = client.getByPaths(*pathSeasons(traktSlug, seasonNumber, "ratings"))

    private fun pathSeasons(traktSlug: String, seasonNumber: Int, vararg paths: String) =
        arrayOf("shows", traktSlug, "seasons", seasonNumber.toString(), *paths)
}
