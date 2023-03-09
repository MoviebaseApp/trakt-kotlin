package app.moviebase.trakt.api

import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktShow
import io.ktor.client.HttpClient

class TraktShowsApi(private val client: HttpClient) {

    suspend fun getSummary(traktSlug: String): TraktShow = client.getByPaths(*pathShows(traktSlug)) {
        parameterExtended()
    }

    suspend fun getRating(traktSlug: String): TraktRating = client.getByPaths(*pathShows(traktSlug, "ratings"))

    private fun pathShows(traktSlug: String, vararg paths: String) = arrayOf("shows", traktSlug, *paths)
}
