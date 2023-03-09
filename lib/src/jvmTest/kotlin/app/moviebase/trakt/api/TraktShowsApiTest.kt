package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktShowsApiTest {

    val client = mockHttpClient(
        responses = mapOf(
            "shows/vikings?extended=full" to "shows/show_summary_vikings.json",
        ),
    )

    val classToTest = TraktShowsApi(client)

    @Test
    fun `it can fetch show summary`() = runTest {
        val traktShow = classToTest.getSummary("vikings", TraktExtended.FULL)

        assertThat(traktShow.title).isEqualTo("Vikings")
    }
}
