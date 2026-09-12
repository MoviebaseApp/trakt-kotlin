package app.moviebase.trakt.api

import app.moviebase.trakt.core.NO_CONTENT
import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktHiddenSection
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.TraktWebConfig
import app.moviebase.trakt.model.TraktUserSlug
import app.moviebase.trakt.model.TraktWatching
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktUsersApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "users/requests" to "users/follow_requests.json",
                    "users/likes?page=1&limit=10" to "users/likes.json",
                    "users/hidden/recommendations?page=1&limit=10" to "users/hidden_items.json",
                    "users/me/ratings/movies?page=1&limit=10" to "users/ratings_movies.json",
                    "users/me/ratings/shows?page=1&limit=10" to "users/ratings_shows.json",
                    "users/me/watchlist/movies?page=1&limit=10" to "users/watchlist_movies.json",
                    "users/me/watchlist/shows?page=1&limit=10" to "users/watchlist_shows.json",
                    "users/me/favorites/movies?page=1&limit=10" to "users/favorites_movies.json",
                    "users/me/favorites/shows?page=1&limit=10" to "users/favorites_shows.json",
                    "users/me/watching" to "users/watching_movie.json",
                    "users/me/notes?page=1&limit=10" to "users/notes.json",
                    "users/me/stats" to "users/stats.json",
                    "users/sean" to "users/profile.json",
                    "users/me/watched/movies?page=1&limit=250" to "sync/watched_movies.json",
                    "users/me/watched/shows?page=1&limit=250" to "sync/watched_shows.json",
                    "users/me/lists?page=1&limit=100" to "users/lists.json",
                    "users/me/collection/movies?page=1&limit=100" to "users/collection_movies.json",
                ),
        )

    val classToTest = TraktUsersApi(client)

    @Test
    fun `it can fetch follower requests`() =
        runTest {
            val requests = classToTest.getFollowerRequests()

            assertThat(requests).isNotEmpty()
            val first = requests.first()
            assertThat(first.id).isEqualTo(12345)
            assertThat(first.user.userName).isEqualTo("johndoe")
        }

    @Test
    fun `it can fetch likes`() =
        runTest {
            val likes = classToTest.getLikes(page = 1, limit = 10)

            assertThat(likes).isNotEmpty()
            val first = likes.first()
            assertThat(first.type).isEqualTo("list")
            assertThat(first.list).isNotNull()
            assertThat(first.list?.name).isEqualTo("Best Sci-Fi Movies")
        }

    @Test
    fun `it can fetch hidden items`() =
        runTest {
            val hidden = classToTest.getHiddenItems(TraktHiddenSection.RECOMMENDATIONS, page = 1, limit = 10)

            assertThat(hidden).isNotEmpty()
            val first = hidden.first()
            assertThat(first.movie).isNotNull()
        }

    @Test
    fun `it can fetch ratings for movies`() =
        runTest {
            val ratings = classToTest.getRatingsMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(ratings).hasSize(2)
            val first = ratings.first()
            assertThat(first.rating).isEqualTo(8)
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("Inception")
            assertThat(first.movie?.year).isEqualTo(2010)
            assertThat(first.movie?.ids?.trakt).isEqualTo(16662)
            assertThat(first.movie?.ids?.imdb).isEqualTo("tt1375666")

            val second = ratings[1]
            assertThat(second.rating).isEqualTo(10)
            assertThat(second.movie?.title).isEqualTo("The Dark Knight")
        }

    @Test
    fun `it can fetch ratings for shows`() =
        runTest {
            val ratings = classToTest.getRatingsShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(ratings).hasSize(1)
            val first = ratings.first()
            assertThat(first.rating).isEqualTo(9)
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("Breaking Bad")
            assertThat(first.show?.year).isEqualTo(2008)
            assertThat(first.show?.ids?.trakt).isEqualTo(1388)
        }

    @Test
    fun `it can fetch watchlist movies`() =
        runTest {
            val watchlist = classToTest.getWatchlistMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(watchlist).hasSize(1)
            val first = watchlist.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.id).isEqualTo(12345678)
            assertThat(first.notes).isEqualTo("Must watch this weekend")
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("Dune: Part Two")
            assertThat(first.movie?.year).isEqualTo(2024)
        }

    @Test
    fun `it can fetch watchlist shows`() =
        runTest {
            val watchlist = classToTest.getWatchlistShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(watchlist).hasSize(1)
            val first = watchlist.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("The Last of Us")
            assertThat(first.show?.year).isEqualTo(2023)
            assertThat(first.show?.ids?.trakt).isEqualTo(158947)
        }

    @Test
    fun `it can fetch favorite movies`() =
        runTest {
            val favorites = classToTest.getFavoriteMovies(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(favorites).hasSize(1)
            val first = favorites.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.id).isEqualTo(11111111)
            assertThat(first.notes).isEqualTo("All-time favorite")
            assertThat(first.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(first.movie).isNotNull()
            assertThat(first.movie?.title).isEqualTo("The Shawshank Redemption")
            assertThat(first.movie?.year).isEqualTo(1994)
        }

    @Test
    fun `it can fetch watching for current user`() =
        runTest {
            val watching = classToTest.getWatching()

            assertThat(watching).isNotNull()
            assertThat(watching!!.action).isEqualTo("watching")
            assertThat(watching.type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(watching.movie).isNotNull()
            assertThat(watching.movie?.title).isEqualTo("Guardians of the Galaxy")
            assertThat(watching.movie?.year).isEqualTo(2014)
            assertThat(watching.movie?.ids?.trakt).isEqualTo(28)
            assertThat(watching.movie?.ids?.imdb).isEqualTo("tt2015381")
            assertThat(watching.expiresAt).isNotNull()
            assertThat(watching.startedAt).isNotNull()
        }

    @Test
    fun `it can fetch favorite shows`() =
        runTest {
            val favorites = classToTest.getFavoriteShows(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(favorites).hasSize(1)
            val first = favorites.first()
            assertThat(first.rank).isEqualTo(1)
            assertThat(first.notes).isEqualTo("Best TV show ever")
            assertThat(first.type).isEqualTo(TraktMediaType.SHOW)
            assertThat(first.show).isNotNull()
            assertThat(first.show?.title).isEqualTo("The Wire")
            assertThat(first.show?.year).isEqualTo(2002)
        }

    @Test
    fun `it can fetch user stats`() =
        runTest {
            val stats = requireNotNull(classToTest.getStats(TraktUserSlug.ME))

            assertThat(stats.movies.plays).isEqualTo(155)
            assertThat(stats.movies.watched).isEqualTo(114)
            assertThat(stats.movies.minutes).isEqualTo(15650)
            assertThat(stats.movies.collected).isEqualTo(933)
            assertThat(stats.shows.watched).isEqualTo(16)
            assertThat(stats.shows.collected).isEqualTo(7)
            assertThat(stats.seasons.ratings).isEqualTo(6)
            assertThat(stats.episodes.watched).isEqualTo(534)
            assertThat(stats.episodes.minutes).isEqualTo(17330)
            assertThat(stats.network.followers).isEqualTo(4)
            assertThat(stats.network.following).isEqualTo(11)
            assertThat(stats.network.friends).isEqualTo(1)
            assertThat(stats.ratings.total).isEqualTo(389)
            assertThat(stats.ratings.distribution["10"]).isEqualTo(63f)
        }

    @Test
    fun `it returns no stats when trakt has not computed them yet`() =
        runTest {
            val noStatsClient = mockHttpClient(mapOf("users/me/stats" to NO_CONTENT))

            val stats = TraktUsersApi(noStatsClient).getStats(TraktUserSlug.ME)

            assertThat(stats).isNull()
        }

    @Test
    fun `it can fetch a user profile`() =
        runTest {
            val user = classToTest.getProfile(TraktUserSlug("sean"))

            assertThat(user.userName).isEqualTo("sean")
            assertThat(user.name).isEqualTo("Sean Rudford")
            assertThat(user.location).isEqualTo("SF")
            assertThat(user.about).isEqualTo("I have all your bases.")
            assertThat(user.gender).isEqualTo("male")
            assertThat(user.age).isEqualTo(35)
            assertThat(user.private).isFalse()
            assertThat(user.vip).isTrue()
            assertThat(user.vipOg).isTrue()
            assertThat(user.vipYears).isEqualTo(5)
            assertThat(user.joinedAt).isNotNull()
            assertThat(user.ids?.slug).isEqualTo("sean")
            assertThat(user.ids?.uuid).isNotNull()
            assertThat(user.imagePath).isNotNull()
        }

    @Test
    fun `it can fetch user notes with nested note and object attached_to`() =
        runTest {
            val notes = classToTest.getUserNotes(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 10,
            )

            assertThat(notes).hasSize(2)

            val movieNote = notes.first()
            assertThat(movieNote.type).isEqualTo("movie")
            assertThat(movieNote.attachedTo?.type).isEqualTo("movie")
            assertThat(movieNote.movie?.title).isEqualTo("Batman Begins")
            assertThat(movieNote.movie?.ids?.tmdb).isEqualTo(272)
            // The note content is nested under `note`, not flattened.
            assertThat(movieNote.note?.id).isEqualTo(49)
            assertThat(movieNote.note?.notes).isEqualTo("Only watch the extended edition.")

            val showNote = notes[1]
            assertThat(showNote.type).isEqualTo("show")
            // attached_to can carry an id (e.g. a specific history play).
            assertThat(showNote.attachedTo?.type).isEqualTo("history")
            assertThat(showNote.attachedTo?.id).isEqualTo(4943432)
            assertThat(showNote.show?.ids?.tmdb).isEqualTo(1399)
            assertThat(showNote.note?.notes).isEqualTo("Rewatch before the finale.")
        }

    @Test
    fun `it sends page and limit when fetching watched movies`() =
        runTest {
            val page = classToTest.getWatchedMoviesPage(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = TraktWebConfig.MAX_LIMIT_WATCHED,
            )

            assertThat(page.items).hasSize(2)
            assertThat(page.items.first().movie?.ids?.tmdb).isEqualTo(564)
        }

    @Test
    fun `it sends page and limit when fetching watched shows`() =
        runTest {
            val page = classToTest.getWatchedShowsPage(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = TraktWebConfig.MAX_LIMIT_WATCHED,
            )

            assertThat(page.items).hasSize(1)
            assertThat(page.items.first().show?.ids?.tmdb).isEqualTo(881)
        }

    @Test
    fun `it sends page and limit when fetching user lists`() =
        runTest {
            val page = classToTest.getListsPage(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 100,
            )

            assertThat(page.items).hasSize(2)
            assertThat(page.items.first().name).isEqualTo("Watch Later")
            assertThat(page.items.first().ids?.trakt).isEqualTo(1234567)
        }

    @Test
    fun `collection movies exposes the pagination headers through the paged twin`() =
        runTest {
            val page = classToTest.getCollectionMoviesPage(
                userSlug = TraktUserSlug.ME,
                page = 1,
                limit = 100,
            )

            assertThat(page.items).hasSize(1)
            assertThat(page.items.first().movie?.ids?.tmdb).isEqualTo(329865)
        }
}
