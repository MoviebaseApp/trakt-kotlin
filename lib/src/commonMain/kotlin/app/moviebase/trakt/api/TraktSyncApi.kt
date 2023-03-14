package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktListMediaType
import app.moviebase.trakt.model.TraktListType
import app.moviebase.trakt.model.TraktMediaItem
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktSyncApi(private val client: HttpClient) {

    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(pathSync("history")) {
        contentType(ContentType.Application.Json)
        setBody(items)
    }

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(pathSync("history", "remove")) {
        contentType(ContentType.Application.Json)
        setBody(items)
    }

    suspend fun getSyncList(
        listType: TraktListType,
        mediaType: TraktListMediaType,
        itemId: Int? = null,
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktMediaItem> = client.getByPaths(pathSyncList(listType, mediaType, itemId)) {
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }

    suspend inline fun getWatchedShows(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHED,
        mediaType = TraktListMediaType.SHOWS,
        extended = extended,
    )

    suspend inline fun getWatchedMovies(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHED,
        mediaType = TraktListMediaType.MOVIES,
        extended = extended,
    )

    suspend inline fun getWatchlistMovies(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHLIST,
        mediaType = TraktListMediaType.MOVIES,
        extended = extended,
    )

    suspend inline fun getWatchlistShows(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHLIST,
        mediaType = TraktListMediaType.SHOWS,
        extended = extended,
    )

    suspend inline fun getWatchlistSeasons(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHLIST,
        mediaType = TraktListMediaType.SEASONS,
        extended = extended,
    )

    suspend inline fun getWatchlistEpisodes(
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = getSyncList(
        listType = TraktListType.WATCHLIST,
        mediaType = TraktListMediaType.EPISODES,
        extended = extended,
    )

    private fun pathSync(vararg paths: String?) = arrayOf("sync", *paths).filterNotNull()

    private fun pathSyncList(
        listType: TraktListType,
        mediaType: TraktListMediaType,
        itemId: Int? = null,
    ) = pathSync(listType.value, mediaType.value, itemId?.toString())
}
