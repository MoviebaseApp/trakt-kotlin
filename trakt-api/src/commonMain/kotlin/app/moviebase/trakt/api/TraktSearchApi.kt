package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSearchResult
import app.moviebase.trakt.model.TraktSearchType
import app.moviebase.trakt.remote.endPoint
import io.ktor.client.*
import io.ktor.client.request.*

class TraktSearchApi(private val client: HttpClient) {

    suspend fun search(searchType: TraktSearchType, id: String, mediaType: TraktMediaType): List<TraktSearchResult> = client.get {
        endPointSearch(searchType.value, id)
        parameter("type", mediaType.value)
    }

    private fun HttpRequestBuilder.endPointSearch(vararg paths: String) {
        endPoint("search", *paths)
    }
}
