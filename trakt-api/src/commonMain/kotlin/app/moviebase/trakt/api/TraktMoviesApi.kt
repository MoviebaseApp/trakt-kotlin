package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktMovie
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.remote.endPoint
import app.moviebase.trakt.remote.parameterExtended
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TraktMoviesApi(private val client: HttpClient) {

    suspend fun getSummary(traktSlug: String): TraktMovie = client.get {
        endPointMovies(traktSlug)
        parameterExtended()
    }.body()

    suspend fun getRating(traktSlug: String): TraktRating = client.get {
        endPointMovies(traktSlug, "ratings")
    }.body()

    private fun HttpRequestBuilder.endPointMovies(traktSlug: String, vararg paths: String) {
        endPoint("movies", traktSlug, *paths)
    }
}
