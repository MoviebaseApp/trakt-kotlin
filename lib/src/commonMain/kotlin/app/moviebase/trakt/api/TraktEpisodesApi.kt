package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient

class TraktEpisodesApi(private val client: HttpClient) {

    suspend fun getSummary(
        traktSlug: String,
        seasonNumber: Int,
        episodeNumber: Int,
        extended: TraktExtended = TraktExtended.FULL,
    ): TraktEpisode = client.getByPaths(*pathEpisodes(traktSlug, seasonNumber, episodeNumber)) {
        parameterExtended(extended)
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
