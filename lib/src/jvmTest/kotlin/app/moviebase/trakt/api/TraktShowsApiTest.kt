package app.moviebase.trakt.api

import app.moviebase.trakt.remote.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

class TraktShowsApiTest {

    val client = mockHttpClient(
        responses = mapOf(
            "shows/vikings?extended=full" to "shows/show_summary_vikings.json",
        ),
    )

    val classToTest = TraktShowsApi(client)

    @Test
    fun `it can fetch show summary`() = runBlocking {
        val traktShow = classToTest.getSummary("vikings")

        assertThat(traktShow.title).isEqualTo("Vikings")
    }
}
