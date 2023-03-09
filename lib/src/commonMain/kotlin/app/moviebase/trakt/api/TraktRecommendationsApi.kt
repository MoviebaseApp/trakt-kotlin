package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktShow
import io.ktor.client.HttpClient

class TraktRecommendationsApi(private val client: HttpClient) {

    suspend fun getShows(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.getByPaths(*pathRecommendations("shows")) {
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }

    private fun pathRecommendations(vararg paths: String) = arrayOf("recommendations", *paths)
}
