package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktAnticipatedShow
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktShow
import app.moviebase.trakt.model.TraktTrendingShow
import io.ktor.client.HttpClient

class TraktShowsApi(private val client: HttpClient) {

    suspend fun getTrending(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktTrendingShow> = client.getByPaths(*pathShows("trending")) {
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }

    suspend fun getPopular(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.getByPaths(*pathShows("popular")) {
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }

    suspend fun getAnticipated(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktAnticipatedShow> = client.getByPaths(*pathShows("anticipated")) {
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }

    suspend fun getRelated(
        showId: String,
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.getByPaths(*pathShows(showId, "related")) {
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }

    suspend fun getSummary(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktShow = client.getByPaths(*pathShows(showId)) {
        extended?.let { parameterExtended(it) }
    }

    suspend fun getRating(traktSlug: String): TraktRating = client.getByPaths(*pathShows(traktSlug, "ratings"))

    private fun pathShows(traktSlug: String, vararg paths: String) = arrayOf("shows", traktSlug, *paths)
}
