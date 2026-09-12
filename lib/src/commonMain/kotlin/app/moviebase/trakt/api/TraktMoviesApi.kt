package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.TraktPage
import app.moviebase.trakt.core.bodyPage
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktAlias
import app.moviebase.trakt.model.TraktAnticipatedMovie
import app.moviebase.trakt.model.TraktBoxOfficeMovie
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktCommentSort
import app.moviebase.trakt.model.TraktCredits
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktMovie
import app.moviebase.trakt.model.TraktMovieCounts
import app.moviebase.trakt.model.TraktMovieUpdate
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktRelease
import app.moviebase.trakt.model.TraktStats
import app.moviebase.trakt.model.TraktStudio
import app.moviebase.trakt.model.TraktTranslation
import app.moviebase.trakt.model.TraktTrendingMovie
import app.moviebase.trakt.model.TraktUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class TraktMoviesApi(
    private val client: HttpClient,
) {

    suspend fun getTrending(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktTrendingMovie> = client.get {
        endPointMovies("trending")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getPopular(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> = client.get {
        endPointMovies("popular")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAnticipated(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktAnticipatedMovie> = client.get {
        endPointMovies("anticipated")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getPlayed(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovieCounts> = client.get {
        endPointMovies("played")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getWatched(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovieCounts> = client.get {
        endPointMovies("watched")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getCollected(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovieCounts> = client.get {
        endPointMovies("collected")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getRelated(
        movieId: String,
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktMovie> = client.get {
        endPointMovie(movieId, "related")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getSummary(
        traktSlug: String,
        extended: TraktExtended? = null,
    ): TraktMovie = client.get {
        endPointMovies(traktSlug)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getRating(
        traktSlug: String,
        extended: TraktExtended? = null,
    ): TraktRating {
        val response = client.get {
            endPointMovie(traktSlug, "ratings")
            extended?.let { parameterExtended(it) }
        }
        if (response.status == HttpStatusCode.NoContent) return TraktRating()
        return response.body()
    }

    suspend fun getStats(traktSlug: String): TraktStats = client.get {
        endPointMovie(traktSlug, "stats")
    }.body()

    suspend fun getBoxOffice(
        extended: TraktExtended? = null,
    ): List<TraktBoxOfficeMovie> = client.get {
        endPointMovies("boxoffice")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getUpdates(
        startDate: String? = null,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktMovieUpdate> = client.get {
        if (startDate != null) {
            endPointMovies("updates", startDate)
        } else {
            endPointMovies("updates")
        }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getAliases(movieId: String): List<TraktAlias> = client.get {
        endPointMovie(movieId, "aliases")
    }.body()

    suspend fun getReleases(
        movieId: String,
        country: String? = null,
    ): List<TraktRelease> = client.get {
        if (country != null) {
            endPointMovie(movieId, "releases", country)
        } else {
            endPointMovie(movieId, "releases")
        }
    }.body()

    suspend fun getTranslations(
        movieId: String,
        language: String? = null,
    ): List<TraktTranslation> = client.get {
        if (language != null) {
            endPointMovie(movieId, "translations", language)
        } else {
            endPointMovie(movieId, "translations")
        }
    }.body()

    suspend fun getComments(
        movieId: String,
        sort: TraktCommentSort = TraktCommentSort.NEWEST,
        page: Int = 1,
        limit: Int = 10,
    ): TraktPage<TraktComment> = client.get {
        endPointMovie(movieId, "comments", sort.value)
        parameterPage(page)
        parameterLimit(limit)
    }.bodyPage()

    suspend fun getLists(
        movieId: String,
        type: String = "personal",
        sort: String = "popular",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktList> = client.get {
        endPointMovie(movieId, "lists", type, sort)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getPeople(
        movieId: String,
        extended: TraktExtended? = null,
    ): TraktCredits = client.get {
        endPointMovie(movieId, "people")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getUsersWatching(
        movieId: String,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointMovie(movieId, "watching")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getStudios(movieId: String): List<TraktStudio> = client.get {
        endPointMovie(movieId, "studios")
    }.body()

    private fun HttpRequestBuilder.endPointMovie(
        movieId: String,
        vararg paths: String,
    ) {
        endPoint("movies", movieId, *paths)
    }

    private fun HttpRequestBuilder.endPointMovies(vararg paths: String) {
        endPoint("movies", *paths)
    }
}
