package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktSeason
import io.ktor.client.HttpClient

class TraktSeasonsApi(private val client: HttpClient) {

    /**
     * Returns all seasons for a show including the number of episodes in each season.
     */
    suspend fun getSummary(
        showId: String,
        extended: TraktExtended? = null,
    ): List<TraktSeason> = client.getByPaths("shows", showId, "seasons") {
        extended?.let { parameterExtended(it) }
    }

    /**
     * Returns all episodes for a specific season of a show.
     */
    suspend fun getSeason(
        showId: String,
        seasonNumber: Int,
        extended: TraktExtended? = null,
    ): List<TraktEpisode> = client.getByPaths(*pathSeasons(showId, seasonNumber)) {
        extended?.let { parameterExtended(it) }
    }

    /**
     * Returns rating (between 0 and 10) and distribution for a season.
     * @param showId trakt ID, trakt slug, or IMDB ID. Example: "game-of-thrones".
     */
    suspend fun getRatings(
        showId: String,
        seasonNumber: Int,
    ): TraktRating = client.getByPaths(*pathSeasons(showId, seasonNumber, "ratings"))

    /**
     * Path: /shows/id/seasons/season/...
     */
    private fun pathSeasons(showId: String, seasonNumber: Int, vararg paths: String) =
        arrayOf("shows", showId, "seasons", seasonNumber.toString(), *paths)
}
