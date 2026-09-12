package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.TraktPage
import app.moviebase.trakt.core.bodyPage
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktAlias
import app.moviebase.trakt.model.TraktAnticipatedShow
import app.moviebase.trakt.model.TraktShowCertification
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktCommentSort
import app.moviebase.trakt.model.TraktCredits
import app.moviebase.trakt.model.TraktEpisode
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktRating
import app.moviebase.trakt.model.TraktStats
import app.moviebase.trakt.model.TraktShow
import app.moviebase.trakt.model.TraktShowCounts
import app.moviebase.trakt.model.TraktShowProgress
import app.moviebase.trakt.model.TraktShowUpdate
import app.moviebase.trakt.model.TraktStudio
import app.moviebase.trakt.model.TraktTranslation
import app.moviebase.trakt.model.TraktTrendingShow
import app.moviebase.trakt.model.TraktUser
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

class TraktShowsApi(
    private val client: HttpClient,
) {
    suspend fun getTrending(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktTrendingShow> = client.get {
        endPointShows("trending")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getPopular(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.get {
        endPointShows("popular")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getAnticipated(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktAnticipatedShow> = client.get {
        endPointShows("anticipated")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getPlayed(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShowCounts> = client.get {
        endPointShows("played")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getWatched(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShowCounts> = client.get {
        endPointShows("watched")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getCollected(
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShowCounts> = client.get {
        endPointShows("collected")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getRelated(
        showId: String,
        page: Int,
        limit: Int,
        extended: TraktExtended? = null,
    ): List<TraktShow> = client.get {
        endPointShow(showId, "related")
        parameterPage(page)
        parameterLimit(limit)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getSummary(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktShow = client.get {
        endPointShows(showId)
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getRating(
        traktSlug: String,
        extended: TraktExtended? = null,
    ): TraktRating {
        val response = client.get {
            endPointShow(traktSlug, "ratings")
            extended?.let { parameterExtended(it) }
        }
        if (response.status == HttpStatusCode.NoContent) return TraktRating()
        return response.body()
    }

    suspend fun getStats(traktSlug: String): TraktStats = client.get {
        endPointShow(traktSlug, "stats")
    }.body()

    suspend fun getTranslations(
        showId: String,
        language: String? = null,
    ): List<TraktTranslation> = client.get {
        if (language != null) {
            endPointShow(showId, "translations", language)
        } else {
            endPointShow(showId, "translations")
        }
    }.body()

    suspend fun getComments(
        showId: String,
        sort: TraktCommentSort = TraktCommentSort.NEWEST,
        page: Int = 1,
        limit: Int = 10,
    ): TraktPage<TraktComment> = client.get {
        endPointShow(showId, "comments", sort.value)
        parameterPage(page)
        parameterLimit(limit)
    }.bodyPage()

    suspend fun getProgress(
        showId: String,
        hidden: Boolean = false,
        specials: Boolean = false,
        countSpecials: Boolean = true,
    ): TraktShowProgress = client.get {
        endPointShow(showId, "progress", "watched")
        parameter("hidden", hidden)
        parameter("specials", specials)
        parameter("count_specials", countSpecials)
    }.body()

    suspend fun getUpdates(
        startDate: String? = null,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktShowUpdate> = client.get {
        if (startDate != null) {
            endPointShows("updates", startDate)
        } else {
            endPointShows("updates")
        }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getAliases(showId: String): List<TraktAlias> = client.get {
        endPointShow(showId, "aliases")
    }.body()

    suspend fun getCertifications(showId: String): List<TraktShowCertification> = client.get {
        endPointShow(showId, "certifications")
    }.body()

    suspend fun getNextEpisode(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktEpisode? {
        val response = client.get {
            endPointShow(showId, "next_episode")
            extended?.let { parameterExtended(it) }
        }
        if (response.status == HttpStatusCode.NoContent) return null
        return response.body()
    }

    suspend fun getLastEpisode(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktEpisode? {
        val response = client.get {
            endPointShow(showId, "last_episode")
            extended?.let { parameterExtended(it) }
        }
        if (response.status == HttpStatusCode.NoContent) return null
        return response.body()
    }

    suspend fun getLists(
        showId: String,
        type: String = "personal",
        sort: String = "popular",
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktList> = client.get {
        endPointShow(showId, "lists", type, sort)
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getPeople(
        showId: String,
        extended: TraktExtended? = null,
    ): TraktCredits = client.get {
        endPointShow(showId, "people")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getUsersWatching(
        showId: String,
        extended: TraktExtended? = null,
    ): List<TraktUser> = client.get {
        endPointShow(showId, "watching")
        extended?.let { parameterExtended(it) }
    }.body()

    suspend fun getStudios(showId: String): List<TraktStudio> = client.get {
        endPointShow(showId, "studios")
    }.body()

    private fun HttpRequestBuilder.endPointShow(
        traktSlug: String,
        vararg paths: String,
    ) {
        endPoint("shows", traktSlug, *paths)
    }

    private fun HttpRequestBuilder.endPointShows(vararg paths: String) {
        endPoint("shows", *paths)
    }
}
