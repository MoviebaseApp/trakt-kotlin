package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktListType
import app.moviebase.trakt.model.TraktMediaItem
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktSyncResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class TraktSyncApi(private val client: HttpClient) {

    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(*pathSync("history")) {
        setBody(items)
    }

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(*pathSync("history", "remove")) {
        setBody(items)
    }

    suspend fun getSyncList(
        listType: TraktListType,
        mediaType: TraktMediaType,
        extended: TraktExtended? = null,
    ): List<TraktMediaItem> = client.getByPaths(*pathSyncList(listType, mediaType)) {
        extended?.let { parameterExtended(it) }
    }

    suspend fun getWatchedShows(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHED, TraktMediaType.SHOW, extended)
    suspend fun getWatchedMovies(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHED, TraktMediaType.MOVIE, extended)

    suspend fun getWatchlistMovies(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHLIST, TraktMediaType.MOVIE, extended)
    suspend fun getWatchlistShows(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHLIST, TraktMediaType.SHOW, extended)
    suspend fun getWatchlistSeasons(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHLIST, TraktMediaType.SEASON, extended)
    suspend fun getWatchlistEpisodes(extended: TraktExtended? = null) = getSyncList(TraktListType.WATCHLIST, TraktMediaType.EPISODE, extended)

    private fun pathSync(vararg paths: String) = arrayOf("sync", *paths)

    private fun pathSyncList(
        listType: TraktListType,
        mediaType: TraktMediaType,
    ) = pathSync(listType.value, mediaType.path)
}
