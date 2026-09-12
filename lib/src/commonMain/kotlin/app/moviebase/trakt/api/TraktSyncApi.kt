package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.TraktPage
import app.moviebase.trakt.core.bodyPage
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktCollectionItem
import app.moviebase.trakt.model.TraktFavoriteItem
import app.moviebase.trakt.model.TraktLastActivities
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.pathSegment
import app.moviebase.trakt.model.TraktRatedItem
import app.moviebase.trakt.model.TraktWatchedItem
import app.moviebase.trakt.model.TraktWatchlistItem
import app.moviebase.trakt.model.TraktHistoryItem
import app.moviebase.trakt.model.TraktPlaybackItem
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import kotlin.time.Instant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktSyncApi(
    private val client: HttpClient,
) {

    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("history")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("history", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun getHistory(
        type: TraktMediaType? = null,
        itemId: Int? = null,
        startAt: Instant? = null,
        endAt: Instant? = null,
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktHistoryItem> = client.get {
        when {
            type != null && itemId != null -> endPointSync("history", type.pathSegment, itemId.toString())
            type != null -> endPointSync("history", type.pathSegment)
            else -> endPointSync("history")
        }
        startAt?.let { parameter("start_at", it.toString()) }
        endAt?.let { parameter("end_at", it.toString()) }
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.body()

    suspend fun getHistoryPage(
        type: TraktMediaType? = null,
        itemId: Int? = null,
        startAt: Instant? = null,
        endAt: Instant? = null,
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktHistoryItem> = client.get {
        when {
            type != null && itemId != null -> endPointSync("history", type.pathSegment, itemId.toString())
            type != null -> endPointSync("history", type.pathSegment)
            else -> endPointSync("history")
        }

        startAt?.let { parameter("start_at", it.toString()) }
        endAt?.let { parameter("end_at", it.toString()) }
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun addToWatchlist(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("watchlist")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromWatchlist(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("watchlist", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun addToCollection(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("collection")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromCollection(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("collection", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun rateItems(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("ratings")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeRatings(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("ratings", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun addToFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("favorites")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    suspend fun removeFromFavorites(items: TraktSyncItems): TraktSyncResponse = client.post {
        endPointSync("favorites", "remove")
        contentType(ContentType.Application.Json)
        setBody(items)
    }.body()

    // Watched endpoints

    /**
     * Paginated since 2026-07-03. Without page/limit Trakt returns only the FIRST 100 items, so a
     * caller that needs the user's whole watched set must page — prefer [getWatchedShowsPage].
     * Max limit is 250; larger values are not honoured. Trakt's OpenAPI spec does not list these
     * params yet, but the change announcement and its `?page=1&limit=250` example do.
     */
    suspend fun getWatchedShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchedItem> = getWatchedShowsPage(extended, page, limit).items

    suspend fun getWatchedShowsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchedItem> = client.get {
        endPointSync("watched", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    /**
     * Paginated since 2026-07-03. Without page/limit Trakt returns only the FIRST 100 items, so a
     * caller that needs the user's whole watched set must page — prefer [getWatchedMoviesPage].
     * Max limit is 250; larger values are not honoured. Trakt's OpenAPI spec does not list these
     * params yet, but the change announcement and its `?page=1&limit=250` example do.
     */
    suspend fun getWatchedMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchedItem> = getWatchedMoviesPage(extended, page, limit).items

    suspend fun getWatchedMoviesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchedItem> = client.get {
        endPointSync("watched", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    // Watchlist endpoints

    suspend fun getWatchlistMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = getWatchlistMoviesPage(extended, page, limit).items

    suspend fun getWatchlistMoviesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getWatchlistShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = getWatchlistShowsPage(extended, page, limit).items

    suspend fun getWatchlistShowsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getWatchlistSeasons(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = getWatchlistSeasonsPage(extended, page, limit).items

    suspend fun getWatchlistSeasonsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "seasons")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getWatchlistEpisodes(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktWatchlistItem> = getWatchlistEpisodesPage(extended, page, limit).items

    suspend fun getWatchlistEpisodesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktWatchlistItem> = client.get {
        endPointSync("watchlist", "episodes")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    // Collection endpoints

    suspend fun getCollectionMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktCollectionItem> = getCollectionMoviesPage(extended, page, limit).items

    suspend fun getCollectionMoviesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktCollectionItem> = client.get {
        endPointSync("collection", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getCollectionShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktCollectionItem> = getCollectionShowsPage(extended, page, limit).items

    suspend fun getCollectionShowsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktCollectionItem> = client.get {
        endPointSync("collection", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getFavoriteMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktFavoriteItem> = getFavoriteMoviesPage(extended, page, limit).items

    suspend fun getFavoriteMoviesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktFavoriteItem> = client.get {
        endPointSync("favorites", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getFavoriteShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktFavoriteItem> = getFavoriteShowsPage(extended, page, limit).items

    suspend fun getFavoriteShowsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktFavoriteItem> = client.get {
        endPointSync("favorites", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    // Ratings endpoints

    suspend fun getRatedMovies(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = getRatedMoviesPage(extended, page, limit).items

    suspend fun getRatedMoviesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktRatedItem> = client.get {
        endPointSync("ratings", "movies")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getRatedShows(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = getRatedShowsPage(extended, page, limit).items

    suspend fun getRatedShowsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktRatedItem> = client.get {
        endPointSync("ratings", "shows")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getRatedSeasons(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = getRatedSeasonsPage(extended, page, limit).items

    suspend fun getRatedSeasonsPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktRatedItem> = client.get {
        endPointSync("ratings", "seasons")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getRatedEpisodes(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktRatedItem> = getRatedEpisodesPage(extended, page, limit).items

    suspend fun getRatedEpisodesPage(
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): TraktPage<TraktRatedItem> = client.get {
        endPointSync("ratings", "episodes")
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }.bodyPage()

    suspend fun getLastActivities(): TraktLastActivities = client.get {
        endPointSync("last_activities")
    }.body()

    suspend fun getPlaybackProgress(
        type: TraktMediaType? = null,
        limit: Int? = null,
    ): List<TraktPlaybackItem> = client.get {
        if (type != null) {
            endPointSync("playback", type.pathSegment)
        } else {
            endPointSync("playback")
        }
        limit?.let { parameter("limit", it) }
    }.body()

    suspend fun removePlaybackProgress(playbackId: Int) = client.delete {
        endPointSync("playback", playbackId.toString())
    }

    private fun HttpRequestBuilder.endPointSync(vararg paths: String) {
        endPoint("sync", *paths)
    }
}
