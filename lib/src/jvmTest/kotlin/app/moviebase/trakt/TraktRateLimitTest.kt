package app.moviebase.trakt

import app.moviebase.trakt.core.JsonFactory
import app.moviebase.trakt.model.TraktShow
import com.google.common.truth.Truth.assertThat
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.junit.jupiter.api.Test

class TraktRateLimitTest {

    val result = TraktShow(title = "Vikings")
    val show = JsonFactory.create().encodeToString(result)

    @Test
    fun `it can fetches with one retry of one second`() = runBlocking {
        val config = mockHttpErrorConfig(
            result = show,
            retryAfterSeconds = 1
        )
        val client = Trakt(config)

        val timeInMillis = measureTimeMillis {
            val traktShow = client.shows.getSummary("vikings", TraktExtended.FULL)
            assertThat(traktShow.title).isEqualTo("Vikings")
        }

        assertThat(timeInMillis).isGreaterThan(1000L)
    }

    @Test
    fun `it can fetches with three retries and without delays`() = runTest {
        val config = mockHttpErrorConfig(
            result = show,
            timesToFail = 3,
            retryAfterSeconds = 1
        )
        val client = Trakt(config)

        val traktShow = client.shows.getSummary("vikings", TraktExtended.FULL)
        assertThat(traktShow.title).isEqualTo("Vikings")
    }

    private fun mockHttpErrorConfig(
        result: String,
        timesToFail: Int = 1,
        retryAfterSeconds: Int = 1
    ): TraktClientConfig.() -> Unit = {
        traktApiKey = "someKey"
        useCache = true
        useTimeout = true

        maxRetries = 3

        var times = timesToFail

        httpClient(MockEngine) {
            logging {
                logger = TestLogger()
                level = LogLevel.HEADERS
            }

            engine {
                addHandler { _ ->
                    if(times > 0) {
                        val headers = headersOf(
                            "Content-Type" to listOf(ContentType.Application.Json.toString()),
                            "X-Ratelimit" to listOf("""{"name":"UNAUTHED_API_GET_LIMIT","period":300,"limit":1000,"remaining":0,"until":"2020-10-10T00:24:00Z"}"""),
                            "Retry-After" to listOf(retryAfterSeconds.toString()) // after 1 seconds
                        )
                        times--
                        respondError(HttpStatusCode.TooManyRequests, headers = headers)
                    } else {
                        val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                        respond(content = result, headers = headers)
                    }
                }
            }
        }
    }
}
