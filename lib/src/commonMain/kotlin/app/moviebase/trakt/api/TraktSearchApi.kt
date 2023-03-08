package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSearchResult
import app.moviebase.trakt.model.TraktSearchType
import app.moviebase.trakt.remote.endPoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class TraktSearchApi(private val client: HttpClient) {

    /**
     * Lookup items by their Trakt, IMDB, TMDB, TVDB, or TVRage ID.
     *
     * URL example: /search/tmdb/:id?type=movie
     *
     * @see [Search - ID Lookup](https://trakt.docs.apiary.io/#reference/search/id-lookup)
     */
    suspend fun searchIdLookup(searchType: TraktSearchType, id: String, mediaType: TraktMediaType): List<TraktSearchResult> = client.get {
        endPointSearch(searchType.value, id)
        parameter("type", mediaType.value)
    }.body()

    private fun HttpRequestBuilder.endPointSearch(vararg paths: String) {
        endPoint("search", *paths)
    }
}
