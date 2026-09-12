package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.JsonFactory
import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktMoviesApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "movies/boxoffice" to "movies/boxoffice.json",
                    "movies/played?page=1&limit=10" to "movies/played.json",
                    "movies/watched?page=1&limit=10" to "movies/watched.json",
                    "movies/collected?page=1&limit=10" to "movies/collected.json",
                    "movies/dune-part-two-2024/aliases" to "movies/aliases.json",
                    "movies/dune-part-two-2024/releases" to "movies/releases.json",
                    "movies/dune-part-two-2024/releases/us" to "movies/releases.json",
                    "movies/dune-part-two-2024/translations" to "movies/translations.json",
                    "movies/dune-part-two-2024/people" to "movies/people.json",
                    "movies/dune-part-two-2024/studios" to "movies/studios.json",
                    "movies/dune-part-two-2024/ratings" to "movies/rating_empty.json",
                    "movies/dune-part-two-2024/ratings?extended=all" to "movies/rating_extended.json",
                ),
        )

    val classToTest = TraktMoviesApi(client)

    @Test
    fun `it can fetch box office movies`() =
        runTest {
            val movies = classToTest.getBoxOffice()

            assertThat(movies).isNotEmpty()
            val first = movies.first()
            assertThat(first.revenue).isEqualTo(150000000)
            assertThat(first.movie.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch movie aliases`() =
        runTest {
            val aliases = classToTest.getAliases("dune-part-two-2024")

            assertThat(aliases).isNotEmpty()
            val first = aliases.first()
            assertThat(first.title).isEqualTo("Dune 2")
            assertThat(first.country).isEqualTo("us")
        }

    @Test
    fun `it can fetch movie releases`() =
        runTest {
            val releases = classToTest.getReleases("dune-part-two-2024")

            assertThat(releases).isNotEmpty()
            val first = releases.first()
            assertThat(first.country).isEqualTo("us")
            assertThat(first.certification).isEqualTo("PG-13")
            assertThat(first.releaseType).isEqualTo("theatrical")
        }

    @Test
    fun `it can fetch movie releases for specific country`() =
        runTest {
            val releases = classToTest.getReleases("dune-part-two-2024", "us")

            assertThat(releases).isNotEmpty()
        }

    @Test
    fun `it can fetch movie translations`() =
        runTest {
            val translations = classToTest.getTranslations("dune-part-two-2024")

            assertThat(translations).isNotEmpty()
            val first = translations.first()
            assertThat(first.language).isEqualTo("en")
            assertThat(first.title).isEqualTo("Dune: Part Two")
        }

    @Test
    fun `it can fetch movie people`() =
        runTest {
            val credits = classToTest.getPeople("dune-part-two-2024")

            assertThat(credits.cast).isNotEmpty()
            val firstCast = credits.cast.first()
            assertThat(firstCast.character).isEqualTo("Paul Atreides")
            assertThat(firstCast.person.name).isEqualTo("Timothee Chalamet")

            assertThat(credits.crew?.directing).isNotEmpty()
        }

    @Test
    fun `it can fetch movie rating when api returns empty object`() =
        runTest {
            val rating = classToTest.getRating("dune-part-two-2024")

            assertThat(rating.rating).isEqualTo(0.0)
            assertThat(rating.votes).isEqualTo(0)
            assertThat(rating.distribution).isNull()
        }

    @Test
    fun `it requests the extended parameter when asked for all sources`() =
        runTest {
            val rating = classToTest.getRating("dune-part-two-2024", TraktExtended.ALL)

            assertThat(rating.rottenTomatoes?.rating).isEqualTo(51)
            assertThat(rating.rottenTomatoes?.state).isEqualTo("rotten")
            assertThat(rating.rottenTomatoes?.userRating).isEqualTo(64)
            assertThat(rating.metascore?.rating).isEqualTo(49)
            assertThat(rating.letterboxd?.rating).isEqualTo(3.26f)
            assertThat(rating.letterboxd?.votes).isEqualTo(444819)
            assertThat(rating.rottenTomatoes?.userState).isEqualTo("upright")
            assertThat(rating.metascore?.link).contains("imdb.com")
            assertThat(rating.imdb?.votes).isEqualTo(382069)
            assertThat(rating.mal?.rating).isNull()
        }

    @Test
    fun `it resolves the nested trakt rating an extended response returns`() =
        runTest {
            val rating = classToTest.getRating("dune-part-two-2024", TraktExtended.ALL)

            assertThat(rating.rating).isEqualTo(0.0)
            assertThat(rating.votes).isEqualTo(0)
            assertThat(rating.resolvedRating).isEqualTo(7.18649)
            assertThat(rating.resolvedVotes).isEqualTo(17561)
            assertThat(rating.resolvedDistribution).isNotNull()
        }

    @Test
    fun `it resolves the flat rating a non extended response returns`() =
        runTest {
            val rating = classToTest.getRating("dune-part-two-2024")

            assertThat(rating.trakt).isNull()
            assertThat(rating.resolvedRating).isEqualTo(rating.rating)
            assertThat(rating.resolvedVotes).isEqualTo(rating.votes)
        }

    @Test
    fun `it can fetch movie rating when api returns 204 No Content`() =
        runTest {
            val noContentClient =
                HttpClient(MockEngine) {
                    install(ContentNegotiation) { json(JsonFactory.create()) }
                    engine {
                        addHandler { respond(content = "", status = HttpStatusCode.NoContent) }
                    }
                }
            val api = TraktMoviesApi(noContentClient)

            val rating = api.getRating("dune-part-two-2024")

            assertThat(rating.rating).isEqualTo(0.0)
            assertThat(rating.votes).isEqualTo(0)
            assertThat(rating.distribution).isNull()
        }

    @Test
    fun `it can fetch movie studios`() =
        runTest {
            val studios = classToTest.getStudios("dune-part-two-2024")

            assertThat(studios).isNotEmpty()
            val first = studios.first()
            assertThat(first.name).isEqualTo("Legendary Pictures")
            assertThat(first.country).isEqualTo("us")
        }

    @Test
    fun `it reads the counts and movie of the most played watched and collected movies`() =
        runTest {
            val played = classToTest.getPlayed(page = 1, limit = 10)
            val watched = classToTest.getWatched(page = 1, limit = 10)
            val collected = classToTest.getCollected(page = 1, limit = 10)

            assertThat(played).hasSize(2)
            assertThat(played.first().movie?.title).isEqualTo("Mayday")
            assertThat(played.first().playCount).isEqualTo(20774)
            assertThat(watched.first().watcherCount).isEqualTo(19141)
            assertThat(collected.first().movie?.ids?.trakt).isEqualTo(916057)
            assertThat(collected.first().collectedCount).isEqualTo(3022)
        }
}
