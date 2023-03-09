package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktSeasonsApiTest {

    val client = mockHttpClient(
        responses = mapOf(
            "shows/the-expanse/seasons/4?extended=full" to "seasons/seasons_episodes_expanse_season_4.json",
        ),
    )

    val classToTest = TraktSeasonsApi(client)

    @Test
    fun `it can fetch season episodes`() = runTest {
        val episodes = classToTest.getEpisodes("the-expanse", 4)

        val episodeOne = episodes.first()
        assertThat(episodeOne.season).isEqualTo(4)
        assertThat(episodeOne.number).isEqualTo(1)
        assertThat(episodeOne.title).isEqualTo("New Terra")
    }
}
