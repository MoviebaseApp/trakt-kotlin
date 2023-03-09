package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktIdType
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktSearchType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktSearchApiTest {

    val client = mockHttpClient(
        responses = mapOf(
            "search/tmdb/63639?type=show" to "search/search_tmdb_expanse.json",
        ),
    )

    val classToTest = TraktSearchApi(client)

    @Test
    fun `it can search tmdb show`() = runTest {
        val results = classToTest.searchIdLookup(
            idType = TraktIdType.TMDB,
            id = "63639",
            searchType = TraktSearchType.SHOW,
        )

        val showResult = results.first()
        assertThat(results.size).isEqualTo(1)

        assertThat(showResult.type).isEqualTo(TraktMediaType.SHOW)
        assertThat(showResult.show?.title).isEqualTo("The Expanse")
    }
}
