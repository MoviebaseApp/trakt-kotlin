package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.mockHttpClient
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktShowsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "shows/vikings?extended=full" to "shows/show_summary_vikings.json",
                    "shows/the-expanse/aliases" to "shows/aliases.json",
                    "shows/the-expanse/certifications" to "shows/certifications.json",
                    "shows/the-expanse/next_episode" to "shows/next_episode.json",
                    "shows/the-expanse/people" to "shows/people.json",
                    "shows/the-expanse/studios" to "shows/studios.json",
                    "shows/breaking-bad/ratings?extended=all" to "shows/rating_extended.json",
                    "shows/played?page=1&limit=10" to "shows/played.json",
                    "shows/watched?page=1&limit=10" to "shows/watched.json",
                    "shows/collected?page=1&limit=10" to "shows/collected.json",
                ),
        )

    val classToTest = TraktShowsApi(client)

    @Test
    fun `it reads every external rating source of a show`() =
        runTest {
            val rating = classToTest.getRating("breaking-bad", TraktExtended.ALL)

            assertThat(rating.rottenTomatoes?.state).isEqualTo("fresh")
            assertThat(rating.rottenTomatoes?.rating).isEqualTo(96)
            assertThat(rating.metascore?.rating).isEqualTo(87)
            assertThat(rating.imdb?.rating).isEqualTo(9.5f)
            assertThat(rating.tmdb?.votes).isEqualTo(18488)
            assertThat(rating.letterboxd).isNull()
            assertThat(rating.mal?.rating).isNull()
        }

    @Test
    fun `it resolves the nested trakt rating of a show`() =
        runTest {
            val rating = classToTest.getRating("breaking-bad", TraktExtended.ALL)

            assertThat(rating.rating).isEqualTo(0.0)
            assertThat(rating.resolvedRating).isEqualTo(9.31405)
            assertThat(rating.resolvedVotes).isEqualTo(71330)
        }

    @Test
    fun `it can fetch show summary`() =
        runTest {
            val traktShow = classToTest.getSummary("vikings", TraktExtended.FULL)

            assertThat(traktShow.title).isEqualTo("Vikings")
        }

    @Test
    fun `it can fetch show aliases`() =
        runTest {
            val aliases = classToTest.getAliases("the-expanse")

            assertThat(aliases).isNotEmpty()
            val first = aliases.first()
            assertThat(first.title).isEqualTo("The Expanse")
            assertThat(first.country).isEqualTo("us")
        }

    @Test
    fun `it can fetch show certifications`() =
        runTest {
            val certifications = classToTest.getCertifications("the-expanse")

            assertThat(certifications).hasSize(21)
            assertThat(certifications.first().country).isEqualTo("at")
            assertThat(certifications.single { it.country == "us" }.certification).isEqualTo("TV-14")
        }

    @Test
    fun `it reads the counts and show of the most played watched and collected shows`() =
        runTest {
            val played = classToTest.getPlayed(page = 1, limit = 10)
            val watched = classToTest.getWatched(page = 1, limit = 10)
            val collected = classToTest.getCollected(page = 1, limit = 10)

            assertThat(played.first().show?.title).isEqualTo("One Piece")
            assertThat(played.first().playCount).isEqualTo(302513)
            assertThat(played.first().collectorCount).isEqualTo(589)
            assertThat(watched.first().show?.ids?.trakt).isEqualTo(157599)
            assertThat(watched.first().watcherCount).isEqualTo(48059)
            assertThat(collected.first().show?.title).isEqualTo("Jimmy Kimmel Live")
            assertThat(collected.first().collectedCount).isEqualTo(7)
        }

    @Test
    fun `it can fetch next episode`() =
        runTest {
            val episode = classToTest.getNextEpisode("the-expanse")

            assertThat(episode).isNotNull()
            assertThat(episode?.season).isEqualTo(5)
            assertThat(episode?.number).isEqualTo(3)
            assertThat(episode?.title).isEqualTo("Mother")
        }

    @Test
    fun `it can fetch show people`() =
        runTest {
            val credits = classToTest.getPeople("the-expanse")

            assertThat(credits.cast).isNotEmpty()
            val firstCast = credits.cast.first()
            assertThat(firstCast.character).isEqualTo("James Holden")
            assertThat(firstCast.person.name).isEqualTo("Steven Strait")
        }

    @Test
    fun `it can fetch show studios`() =
        runTest {
            val studios = classToTest.getStudios("the-expanse")

            assertThat(studios).isNotEmpty()
            val first = studios.first()
            assertThat(first.name).isEqualTo("Alcon Entertainment")
        }
}
