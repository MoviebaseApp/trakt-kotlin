package app.moviebase.trakt

import io.ktor.client.plugins.auth.providers.BearerTokens
import kotlin.test.assertNotNull
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

// TODO: Enable test when move into integration test folder + own source set
@Disabled
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TraktAccountIntegrationTest {

    val authStore = TraktAuthStore()
    val trakt = buildTrakt(authStore = authStore)
    val traktCredentials = createTraktCredentials()

    @BeforeEach
    fun setUp() {
        traktCredentials.accessToken?.let {
            authStore.bearerTokens = BearerTokens(
                accessToken = traktCredentials.accessToken,
                refreshToken = traktCredentials.refreshToken!!,
            )
        }
    }

    @Test
    fun `it can build up a new session`() = runTest {
        val settings = trakt.users.getSettings()

        assertNotNull(settings.user.name)
    }
}
