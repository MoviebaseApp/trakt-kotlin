package app.moviebase.trakt.api

import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktEpisodeSummary
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient

class TraktEpisodesApi(private val client: HttpClient) {

    suspend fun getSummary(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktEpisodeSummary = client.getByPaths(*pathEpisodes(traktSlug, seasonNumber, episodeNumber)) {
        parameterExtended()
    }

    suspend fun getRating(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
    ): TraktRating = client.getByPaths(*pathEpisodes(traktSlug, seasonNumber, episodeNumber, "ratings"))

    private fun pathEpisodes(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        vararg paths: String,
    ) = arrayOf(
        "shows",
        traktSlug,
        "seasons",
        seasonNumber.toString(),
        "episodes",
        episodeNumber.toString(),
        *paths,
    )
}
