package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktMediaType
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktSyncApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "sync/last_activities" to "sync/last_activities.json",
                    "sync/playback" to "sync/playback.json",
                    "sync/playback/movies" to "sync/playback.json",
                    "sync/collection/shows" to "sync/collection_shows.json",
                    "sync/watched/movies?page=2&limit=250" to "sync/watched_movies.json",
                    "sync/watched/shows?page=1&limit=250" to "sync/watched_shows.json",
                ),
        )

    val classToTest = TraktSyncApi(client)

    @Test
    fun `it can fetch last activities`() =
        runTest {
            val activities = classToTest.getLastActivities()

            assertThat(activities.all).isNotNull()
            assertThat(activities.movies).isNotNull()
            assertThat(activities.movies?.watchedAt).isNotNull()
            assertThat(activities.episodes).isNotNull()
            assertThat(activities.shows).isNotNull()
        }

    @Test
    fun `it can fetch playback progress`() =
        runTest {
            val playback = classToTest.getPlaybackProgress()

            assertThat(playback).isNotEmpty()
            val first = playback.first()
            assertThat(first.id).isEqualTo(12345)
            assertThat(first.progress).isEqualTo(45.5f)
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
        }

    @Test
    fun `it can fetch playback progress for movies only`() =
        runTest {
            val playback = classToTest.getPlaybackProgress(type = TraktMediaType.MOVIE)

            assertThat(playback).isNotEmpty()
        }

    @Test
    fun `it can fetch collection shows when nested episodes omit season field`() =
        runTest {
            val collection = classToTest.getCollectionShows()

            assertThat(collection).isNotEmpty()
            val episodes = collection.first().seasons.first().episodes ?: emptyList()
            assertThat(episodes).isNotEmpty()
            assertThat(episodes.first().number).isEqualTo(1)
            assertThat(episodes.first().season).isNull()
        }

    @Test
    fun `it sends page and limit when fetching watched movies`() =
        runTest {
            val page = classToTest.getWatchedMoviesPage(page = 2, limit = 250)

            assertThat(page.items).hasSize(2)
            assertThat(page.items.first().plays).isEqualTo(3)
            assertThat(page.items.first().movie?.ids?.tmdb).isEqualTo(564)
        }

    @Test
    fun `it sends page and limit when fetching watched shows`() =
        runTest {
            val page = classToTest.getWatchedShowsPage(page = 1, limit = 250)

            assertThat(page.items).hasSize(1)
            assertThat(page.items.first().show?.ids?.tmdb).isEqualTo(881)
        }

    @Test
    fun `the watched movies list delegates to the paged call`() =
        runTest {
            val items = classToTest.getWatchedMovies(page = 2, limit = 250)

            assertThat(items).hasSize(2)
        }
}
