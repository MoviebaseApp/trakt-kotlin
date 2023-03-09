package app.moviebase.trakt.api

import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.model.TraktMovie
import app.moviebase.trakt.model.TraktRating
import io.ktor.client.HttpClient

class TraktMoviesApi(private val client: HttpClient) {

    suspend fun getSummary(traktSlug: String): TraktMovie = client.getByPaths(*pathMovies(traktSlug)) {
        parameterExtended()
    }

    suspend fun getRating(traktSlug: String): TraktRating = client.getByPaths(*pathMovies(traktSlug, "ratings"))

    private fun pathMovies(traktSlug: String, vararg paths: String) = arrayOf("movies", traktSlug, *paths)
}
