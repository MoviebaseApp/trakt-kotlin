package app.moviebase.trakt

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TraktShowIntegrationTest {

    val trakt = buildTrakt()

    @Test
    fun `get show summary`() = runTest {
        val show = trakt.shows.getSummary("tom-clancy-s-jack-ryan", TraktExtended.FULL)

        assertThat(show).isNotNull()
    }
}
