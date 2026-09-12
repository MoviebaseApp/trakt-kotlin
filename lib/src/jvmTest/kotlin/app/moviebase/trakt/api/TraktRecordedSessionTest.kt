package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.NO_CONTENT
import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktCommentSort
import app.moviebase.trakt.model.TraktHiddenSection
import app.moviebase.trakt.model.TraktIdType
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktPostComment
import app.moviebase.trakt.model.TraktSearchType
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktUserSlug
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.time.Instant

class TraktRecordedSessionTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "sync/last_activities" to "recorded/last_activities.json",
                    "users/me?extended=full" to "recorded/users_me_full.json",
                    "users/me/stats" to NO_CONTENT,
                    "users/me/watching" to NO_CONTENT,
                    "users/hidden/progress_watched?page=1&limit=250" to "recorded/hidden_progress_watched.json",
                    "users/hidden/recommendations?page=1&limit=250" to "recorded/hidden_recommendations.json",
                    "search/tmdb/36109?type=show" to "recorded/search_tmdb_show.json",
                    "search/tmdb/1204680?type=movie" to "recorded/search_tmdb_movie.json",
                    "movies/970531/comments/likes?page=1&limit=10" to "recorded/movie_comments.json",
                    "shows/180770/comments/likes?page=1&limit=8" to "recorded/show_comments.json",
                    "shows/180770/seasons/1/comments/likes?page=1&limit=8" to "recorded/season_comments.json",
                    "shows/180770/seasons/1/episodes/1/comments/likes?page=1&limit=8" to "recorded/episode_comments.json",
                    "shows/35953/comments/likes?page=1&limit=8" to "recorded/show_comments_empty.json",
                    "shows/180770/seasons/1/episodes/1/ratings" to "recorded/episode_ratings.json",
                    "shows/180770/seasons/1/episodes/1?extended=full" to "recorded/episode_summary_full.json",
                    "shows/35953/seasons?extended=full" to "recorded/show_seasons_full.json",
                    "movies/anticipated?page=1&limit=10" to "recorded/movies_anticipated.json",
                    "recommendations/movies?page=1&limit=100" to "recorded/recommendations_movies.json",
                    "comments/1035361/replies" to "recorded/comment_replies.json",
                ),
        )

    @Test
    fun `it decodes the last activities of the signed-in user`() =
        runTest {
            val activities = TraktSyncApi(client).getLastActivities()

            assertThat(activities.all).isEqualTo(Instant.parse("2026-09-12T16:05:10Z"))
            assertThat(activities.episodes?.watchedAt).isEqualTo(Instant.parse("2026-09-12T10:15:57Z"))
        }

    @Test
    fun `it decodes the signed-in user profile`() =
        runTest {
            val user = TraktUsersApi(client).getProfile(TraktUserSlug.ME, TraktExtended.FULL)

            assertThat(user.userName).isEqualTo("chrisbln_cac11e")
            assertThat(user.ids?.slug).isEqualTo("chrisbln-cac11e")
        }

    @Test
    fun `it returns no stats for an account trakt has not computed yet`() =
        runTest {
            assertThat(TraktUsersApi(client).getStats(TraktUserSlug.ME)).isNull()
        }

    @Test
    fun `it returns nothing watching when trakt answers no content`() =
        runTest {
            assertThat(TraktUsersApi(client).getWatching()).isNull()
        }

    @Test
    fun `it decodes hidden progress items`() =
        runTest {
            val hidden = TraktUsersApi(client).getHiddenItems(TraktHiddenSection.PROGRESS_WATCHED, page = 1, limit = 250)

            assertThat(hidden).hasSize(3)
            assertThat(hidden.first().type).isEqualTo(TraktMediaType.SHOW)
            assertThat(hidden.first().show?.title).isEqualTo("My Life with the Walter Boys")
        }

    @Test
    fun `it decodes hidden recommendations of both media types`() =
        runTest {
            val hidden = TraktUsersApi(client).getHiddenItems(TraktHiddenSection.RECOMMENDATIONS, page = 1, limit = 250)

            assertThat(hidden.map { it.type }).containsExactly(TraktMediaType.MOVIE, TraktMediaType.SHOW)
        }

    @Test
    fun `it decodes the response of hiding items`() =
        runTest {
            val postClient = mockHttpClient(mapOf("users/hidden/progress_watched" to "recorded/hidden_add_progress_watched.json"))

            val response = TraktUsersApi(postClient).addHiddenItems(TraktHiddenSection.PROGRESS_WATCHED, TraktSyncItems())

            assertThat(response.added?.shows).isEqualTo(5)
            assertThat(response.notFound).isNotNull()
        }

    @Test
    fun `it resolves a tmdb show id`() =
        runTest {
            val results = TraktSearchApi(client).searchIdLookup(TraktIdType.TMDB, "36109", TraktSearchType.SHOW)

            assertThat(results.single().show?.ids?.trakt).isEqualTo(35953)
        }

    @Test
    fun `it resolves a tmdb movie id`() =
        runTest {
            val results = TraktSearchApi(client).searchIdLookup(TraktIdType.TMDB, "1204680", TraktSearchType.MOVIE)

            assertThat(results.single().type).isEqualTo(TraktMediaType.MOVIE)
            assertThat(results.single().movie?.ids?.tmdb).isEqualTo(1204680)
        }

    @Test
    fun `it decodes movie comments sorted by likes`() =
        runTest {
            val page = TraktMoviesApi(client).getComments("970531", TraktCommentSort.LIKES, page = 1, limit = 10)

            assertThat(page.items.first().id).isEqualTo(1035361)
        }

    @Test
    fun `it decodes show season and episode comments`() =
        runTest {
            val show = TraktShowsApi(client).getComments("180770", TraktCommentSort.LIKES, page = 1, limit = 8)
            val season = TraktSeasonsApi(client).getComments("180770", 1, TraktCommentSort.LIKES, page = 1, limit = 8)
            val episode = TraktEpisodesApi(client).getComments("180770", 1, 1, TraktCommentSort.LIKES, page = 1, limit = 8)

            assertThat(show.items.first().id).isEqualTo(567824)
            assertThat(season.items.first().spoiler).isTrue()
            assertThat(episode.items.first().id).isEqualTo(562361)
        }

    @Test
    fun `it decodes a show without comments`() =
        runTest {
            val page = TraktShowsApi(client).getComments("35953", TraktCommentSort.LIKES, page = 1, limit = 8)

            assertThat(page.items).isEmpty()
        }

    @Test
    fun `it decodes an episode summary and rating`() =
        runTest {
            val episode = TraktEpisodesApi(client).getSummary("180770", 1, 1)
            val rating = TraktEpisodesApi(client).getRating("180770", 1, 1)

            assertThat(episode.title).isEqualTo("Freedom Day")
            assertThat(episode.runtime).isEqualTo(62)
            assertThat(rating.votes).isEqualTo(5244)
        }

    @Test
    fun `it decodes seasons including specials without an overview`() =
        runTest {
            val seasons = TraktSeasonsApi(client).getSummary("35953", TraktExtended.FULL)

            assertThat(seasons.map { it.number }).containsExactly(0, 1, 2).inOrder()
            assertThat(seasons.first().episodeCount).isEqualTo(8)
        }

    @Test
    fun `it decodes anticipated movies`() =
        runTest {
            val anticipated = TraktMoviesApi(client).getAnticipated(page = 1, limit = 10)

            assertThat(anticipated.first().listCount).isEqualTo(69880)
            assertThat(anticipated.first().movie.title).isEqualTo("Avengers: Doomsday")
        }

    @Test
    fun `it decodes movie recommendations`() =
        runTest {
            val movies = TraktRecommendationsApi(client).getMovies(page = 1, limit = 100)

            assertThat(movies).hasSize(3)
            assertThat(movies.first().ids?.trakt).isEqualTo(293990)
        }

    @Test
    fun `it decodes the replies of a comment`() =
        runTest {
            val replies = TraktCommentsApi(client).getCommentReplies(1035361)

            assertThat(replies).hasSize(3)
            assertThat(replies.map { it.parentId }.toSet()).containsExactly(1035361)
        }

    @Test
    fun `it decodes a posted reply`() =
        runTest {
            val postClient = mockHttpClient(mapOf("comments/1035361/replies" to "recorded/comment_reply_posted.json"))

            val reply = TraktCommentsApi(postClient).postCommentReplies(
                id = 1035361,
                comment = TraktPostComment(comment = "Agree the animation is really gre at", spoiler = false),
            )

            assertThat(reply.id).isEqualTo(1048002)
            assertThat(reply.parentId).isEqualTo(1035361)
        }
}
