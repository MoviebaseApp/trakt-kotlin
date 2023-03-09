package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.core.parameters
import app.moviebase.trakt.model.TraktIdType
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSearchQuery
import app.moviebase.trakt.model.TraktSearchResult
import app.moviebase.trakt.model.TraktSearchType
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class TraktSearchApi(private val client: HttpClient) {

    /**
     * Lookup items by their Trakt, IMDB, TMDB, TVDB, or TVRage ID.
     *
     * URL example: /search/tmdb/:id?type=movie
     *
     * @see [Search - ID Lookup](https://trakt.docs.apiary.io/#reference/search/id-lookup)
     */
    suspend fun searchIdLookup(
        idType: TraktIdType,
        id: String,
        searchType: TraktSearchType,
        extended: TraktExtended? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<TraktSearchResult> = client.getByPaths(*pathSearch(idType.value, id)) {
        parameter("type", searchType.value)
        extended?.let { parameterExtended(it) }
        page?.let { parameterPage(it) }
        limit?.let { parameterLimit(it) }
    }

    suspend fun searchTextQuery(
        mediaType: TraktMediaType,
        searchQuery: TraktSearchQuery,
    ): List<TraktSearchResult> = client.getByPaths(*pathSearch(mediaType.value)) {
        parameters(searchQuery.parameters)
    }

    suspend fun searchTextQueryMovie(searchQuery: TraktSearchQuery) = searchTextQuery(TraktMediaType.MOVIE, searchQuery)
    suspend fun searchTextQueryShow(searchQuery: TraktSearchQuery) = searchTextQuery(TraktMediaType.SHOW, searchQuery)

    private fun pathSearch(vararg paths: String) = arrayOf("search", *paths)
}
