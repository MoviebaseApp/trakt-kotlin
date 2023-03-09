package app.moviebase.trakt.api

import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktSyncResponse
import app.moviebase.trakt.model.TraktSyncItems
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class TraktSyncApi(private val client: HttpClient) {

    suspend fun addWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(*pathSync("history")) {
        setBody(items)
    }

    suspend fun removeWatchedHistory(items: TraktSyncItems): TraktSyncResponse = client.postByPaths(*pathSync("history", "remove")) {
        setBody(items)
    }

    private fun pathSync(vararg paths: String) = arrayOf("sync", *paths)
}
