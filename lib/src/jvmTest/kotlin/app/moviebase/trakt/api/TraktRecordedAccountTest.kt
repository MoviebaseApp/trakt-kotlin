package app.moviebase.trakt.api

import app.moviebase.trakt.core.NO_CONTENT
import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktCheckinItem
import app.moviebase.trakt.model.TraktCheckinMovie
import app.moviebase.trakt.model.TraktHiddenSection
import app.moviebase.trakt.model.TraktItemIds
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktNoteRequest
import app.moviebase.trakt.model.TraktPostComment
import app.moviebase.trakt.model.TraktScrobbleAction
import app.moviebase.trakt.model.TraktScrobbleMovie
import app.moviebase.trakt.model.TraktScrobbleRequest
import app.moviebase.trakt.model.TraktSyncItems
import app.moviebase.trakt.model.TraktUserSlug
import com.google.common.truth.Truth.assertThat
import io.ktor.client.HttpClient
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktRecordedAccountTest {

    private val items = TraktSyncItems()
    private val planNine = TraktItemIds(trakt = 5693)

    @Test
    fun `it decodes the account settings`() =
        runTest {
            val settings = replay("users/settings", "account_settings.json") { TraktUsersApi(it).getSettings() }

            assertThat(settings.user.userName).isEqualTo("chrisbln_cac11e")
            assertThat(settings.account).isNotNull()
        }

    @Test
    fun `it decodes the synced watchlist of every media type`() =
        runTest {
            val movies = replay("sync/watchlist/movies?page=1&limit=100", "watchlist_movies.json") { TraktSyncApi(it).getWatchlistMovies(page = 1, limit = 100) }
            val shows = replay("sync/watchlist/shows?page=1&limit=100", "watchlist_shows.json") { TraktSyncApi(it).getWatchlistShows(page = 1, limit = 100) }
            val seasons = replay("sync/watchlist/seasons?page=1&limit=100", "watchlist_seasons.json") { TraktSyncApi(it).getWatchlistSeasons(page = 1, limit = 100) }
            val episodes = replay("sync/watchlist/episodes?page=1&limit=100", "watchlist_episodes.json") { TraktSyncApi(it).getWatchlistEpisodes(page = 1, limit = 100) }

            assertThat(movies.map { it.type }.toSet()).containsExactly(TraktMediaType.MOVIE)
            assertThat(shows.map { it.type }.toSet()).containsExactly(TraktMediaType.SHOW)
            assertThat(seasons.all { it.type == TraktMediaType.SEASON }).isTrue()
            assertThat(episodes.all { it.type == TraktMediaType.EPISODE }).isTrue()
        }

    @Test
    fun `it decodes the synced ratings of every media type`() =
        runTest {
            val movies = replay("sync/ratings/movies?page=1&limit=100", "ratings_movies.json") { TraktSyncApi(it).getRatedMovies(page = 1, limit = 100) }
            val shows = replay("sync/ratings/shows?page=1&limit=100", "ratings_shows.json") { TraktSyncApi(it).getRatedShows(page = 1, limit = 100) }
            val seasons = replay("sync/ratings/seasons?page=1&limit=100", "ratings_seasons.json") { TraktSyncApi(it).getRatedSeasons(page = 1, limit = 100) }
            val episodes = replay("sync/ratings/episodes?page=1&limit=100", "ratings_episodes.json") { TraktSyncApi(it).getRatedEpisodes(page = 1, limit = 100) }

            assertThat(movies.first().rating).isGreaterThan(0)
            assertThat(shows.first().show).isNotNull()
            assertThat(seasons.all { it.season != null }).isTrue()
            assertThat(episodes.all { it.episode != null }).isTrue()
        }

    @Test
    fun `it decodes the synced watched collection and favorites`() =
        runTest {
            val watchedMovies = replay("sync/watched/movies?page=1&limit=10", "watched_movies.json") { TraktSyncApi(it).getWatchedMovies(page = 1, limit = 10) }
            val watchedShows = replay("sync/watched/shows?page=1&limit=10", "watched_shows.json") { TraktSyncApi(it).getWatchedShows(page = 1, limit = 10) }
            val collectionMovies = replay("sync/collection/movies?page=1&limit=100", "collection_movies.json") { TraktSyncApi(it).getCollectionMovies(page = 1, limit = 100) }
            val collectionShows = replay("sync/collection/shows?page=1&limit=100", "collection_shows.json") { TraktSyncApi(it).getCollectionShows(page = 1, limit = 100) }
            val favoriteMovies = replay("sync/favorites/movies?page=1&limit=100", "favorites_movies.json") { TraktSyncApi(it).getFavoriteMovies(page = 1, limit = 100) }
            val favoriteShows = replay("sync/favorites/shows?page=1&limit=100", "favorites_shows.json") { TraktSyncApi(it).getFavoriteShows(page = 1, limit = 100) }

            assertThat(watchedMovies.first().plays).isAtLeast(1)
            assertThat(watchedShows.first().show).isNotNull()
            assertThat(collectionMovies.first().movie).isNotNull()
            assertThat(collectionShows.first().show).isNotNull()
            assertThat(favoriteMovies.first().movie).isNotNull()
            assertThat(favoriteShows.first().show).isNotNull()
        }

    @Test
    fun `it decodes history`() =
        runTest {
            val history = replay("sync/history?page=1&limit=10", "history.json") { TraktSyncApi(it).getHistory(page = 1, limit = 10) }

            assertThat(history).isNotEmpty()
        }

    @Test
    fun `it filters history by the plural media type segment`() =
        runTest {
            val itemHistory = replay("sync/history/movies/5693", "history_item.json") { TraktSyncApi(it).getHistory(type = TraktMediaType.MOVIE, itemId = 5693) }

            assertThat(itemHistory.single().movie?.ids?.trakt).isEqualTo(5693)
        }

    @Test
    fun `it filters playback by the plural media type segment`() =
        runTest {
            val playback = replay("sync/playback/movies", "playback_after_scrobble.json") { TraktSyncApi(it).getPlaybackProgress(type = TraktMediaType.MOVIE) }

            assertThat(playback.single().progress).isEqualTo(30f)
        }

    @Test
    fun `it decodes adding and removing watchlist items`() =
        runTest {
            val added = replay("sync/watchlist", "watchlist_add.json") { TraktSyncApi(it).addToWatchlist(items) }
            val removed = replay("sync/watchlist/remove", "watchlist_remove.json") { TraktSyncApi(it).removeFromWatchlist(items) }

            assertThat(added.added?.movies).isEqualTo(1)
            assertThat(added.added?.episodes).isEqualTo(1)
            assertThat(removed.deleted?.shows).isEqualTo(1)
        }

    @Test
    fun `it decodes adding and removing history`() =
        runTest {
            val added = replay("sync/history", "history_add.json") { TraktSyncApi(it).addWatchedHistory(items) }
            val removed = replay("sync/history/remove", "history_remove.json") { TraktSyncApi(it).removeWatchedHistory(items) }

            assertThat(added.added?.episodes).isEqualTo(1)
            assertThat(removed.deleted?.movies).isEqualTo(1)
        }

    @Test
    fun `it decodes rating and unrating every media type`() =
        runTest {
            val added = replay("sync/ratings", "ratings_add.json") { TraktSyncApi(it).rateItems(items) }
            val removed = replay("sync/ratings/remove", "ratings_remove.json") { TraktSyncApi(it).removeRatings(items) }

            assertThat(added.added?.seasons).isEqualTo(1)
            assertThat(removed.deleted?.episodes).isEqualTo(1)
        }

    @Test
    fun `it decodes collecting and favoriting`() =
        runTest {
            val collected = replay("sync/collection", "collection_add.json") { TraktSyncApi(it).addToCollection(items) }
            val uncollected = replay("sync/collection/remove", "collection_remove.json") { TraktSyncApi(it).removeFromCollection(items) }
            val favorited = replay("sync/favorites", "favorites_add.json") { TraktSyncApi(it).addToFavorites(items) }
            val unfavorited = replay("sync/favorites/remove", "favorites_remove.json") { TraktSyncApi(it).removeFromFavorites(items) }

            assertThat(collected.added?.movies).isEqualTo(1)
            assertThat(uncollected.deleted?.episodes).isEqualTo(1)
            assertThat(favorited.added?.shows).isEqualTo(1)
            assertThat(unfavorited.deleted?.movies).isEqualTo(1)
        }

    @Test
    fun `it decodes hiding and unhiding`() =
        runTest {
            val hidden = replay("users/hidden/calendar?page=1&limit=100", "hidden_calendar.json") { TraktUsersApi(it).getHiddenItems(TraktHiddenSection.CALENDAR, page = 1, limit = 100) }
            val collected = replay("users/hidden/progress_collected?page=1&limit=100", "hidden_progress_collected.json") { TraktUsersApi(it).getHiddenItems(TraktHiddenSection.PROGRESS_COLLECTED, page = 1, limit = 100) }
            val added = replay("users/hidden/calendar", "hidden_add_calendar.json") { TraktUsersApi(it).addHiddenItems(TraktHiddenSection.CALENDAR, items) }
            val removed = replay("users/hidden/calendar/remove", "hidden_remove_calendar.json") { TraktUsersApi(it).removeHiddenItems(TraktHiddenSection.CALENDAR, items) }
            val unhidden = replay("users/hidden/recommendations/remove", "hidden_remove_recommendation.json") { TraktUsersApi(it).removeHiddenItems(TraktHiddenSection.RECOMMENDATIONS, items) }

            val hiddenShows = replay("users/hidden/progress_watched?type=show&page=1&limit=100", "hidden_calendar.json") {
                TraktUsersApi(it).getHiddenItems(TraktHiddenSection.PROGRESS_WATCHED, type = TraktMediaType.SHOW, page = 1, limit = 100)
            }

            assertThat(hidden.size + collected.size + hiddenShows.size).isAtLeast(0)
            assertThat(added.added?.shows).isEqualTo(1)
            assertThat(removed.deleted?.shows).isEqualTo(1)
            assertThat(unhidden.deleted?.movies).isEqualTo(1)
        }

    @Test
    fun `it decodes personal lists and their items`() =
        runTest {
            val lists = replay("users/me/lists", "lists.json") { TraktUsersApi(it).getLists() }
            val updated = replay("users/me/lists/36798585", "list_update.json") { TraktUsersApi(it).updateList(listId = "36798585", list = TraktList(name = "cc")) }
            val listItems = replay("users/me/lists/36798585/items?page=1&limit=100", "list_items.json") { TraktUsersApi(it).getListItems(listId = "36798585", page = 1, limit = 100) }
            val added = replay("users/me/lists/36798585/items", "list_items_add.json") { TraktUsersApi(it).addListItems(listId = "36798585", items = items) }
            val removed = replay("users/me/lists/36798585/items/remove", "list_items_remove.json") { TraktUsersApi(it).removeListItems(listId = "36798585", items = items) }
            val reorderedItems = replay("users/me/lists/36798585/items/reorder", "list_items_reorder.json") { TraktUsersApi(it).reorderListItems(listId = "36798585", rank = listOf(1L)) }
            val reorderedLists = replay("users/me/lists/reorder", "lists_reorder.json") { TraktUsersApi(it).reorderLists(rank = listOf(1L)) }

            assertThat(lists).isNotEmpty()
            assertThat(updated.name).isEqualTo("cc")
            assertThat(listItems).hasSize(3)
            assertThat(added.added?.movies).isEqualTo(1)
            assertThat(removed.deleted?.shows).isEqualTo(1)
            assertThat(reorderedItems.updated).isEqualTo(3)
            assertThat(reorderedLists.updated).isEqualTo(5)
        }

    @Test
    fun `it decodes the note lifecycle`() =
        runTest {
            val request = TraktNoteRequest(notes = "Watch the director's cut first.")
            val added = replay("notes", "note_add.json") { TraktNotesApi(it).addNote(request) }
            val fetched = replay("notes/238379", "note_get.json") { TraktNotesApi(it).getNote(238379) }
            val updated = replay("notes/238379", "note_update.json") { TraktNotesApi(it).updateNote(238379, request) }
            val notes = replay("users/me/notes?page=1&limit=10", "notes.json") { TraktUsersApi(it).getUserNotes(TraktUserSlug.ME, page = 1, limit = 10) }
            replay("notes/238379", NO_CONTENT) { TraktNotesApi(it).deleteNote(238379) }

            assertThat(added.id).isEqualTo(238379)
            assertThat(fetched.notes).isEqualTo("Watch the director's cut first.")
            assertThat(updated.notes).isEqualTo("Watch the original cut first.")
            assertThat(notes.size).isAtLeast(0)
        }

    @Test
    fun `it decodes the comment lifecycle`() =
        runTest {
            val comment = TraktPostComment(comment = "A cult classic that is so bad it becomes genuinely fun to watch.", spoiler = false)
            val posted = replay("comments", "comment_post_movie.json") { TraktCommentsApi(it).postComment(comment) }
            val postedOnEpisode = replay("comments", "comment_post_episode.json") { TraktCommentsApi(it).postComment(comment) }
            val updated = replay("comments/1048019", "comment_update.json") { TraktCommentsApi(it).updateComment(1048019, comment) }
            val item = replay("comments/1048019/item", "comment_item.json") { TraktCommentsApi(it).getCommentItem(1048019) }
            val reply = replay("comments/1035361/replies", "reply_post.json") { TraktCommentsApi(it).postCommentReplies(1035361, comment) }
            replay("comments/1048019", NO_CONTENT) { TraktCommentsApi(it).deleteComment(1048019) }

            assertThat(posted.parentId).isEqualTo(0)
            assertThat(posted.movie).isNull()
            assertThat(postedOnEpisode.id).isEqualTo(1048021)
            assertThat(updated.comment).endsWith("with friends.")
            assertThat(item.movie?.ids?.trakt).isEqualTo(5693)
            assertThat(reply.parentId).isEqualTo(1035361)
        }

    @Test
    fun `it treats liking and unliking as bodiless`() =
        runTest {
            replay("comments/1035361/like", NO_CONTENT) { TraktCommentsApi(it).likeComment(1035361) }
            replay("comments/1035361/like", NO_CONTENT) { TraktCommentsApi(it).unlikeComment(1035361) }
        }

    @Test
    fun `it decodes the checkin lifecycle`() =
        runTest {
            val item = TraktCheckinItem(movie = TraktCheckinMovie(ids = planNine))
            val movie = replay("checkin", "checkin_movie.json") { TraktCheckinApi(it).postCheckin(item) }
            val episode = replay("checkin", "checkin_episode.json") { TraktCheckinApi(it).postCheckin(item) }
            val watching = replay("users/me/watching", "watching_movie.json") { TraktUsersApi(it).getWatching() }
            replay("checkin", NO_CONTENT) { TraktCheckinApi(it).deleteCheckin() }

            assertThat(movie.id).isEqualTo(14411544578)
            assertThat(episode.watchedAt).isNotNull()
            assertThat(watching?.action).isEqualTo("checkin")
            assertThat(watching?.movie?.ids?.trakt).isEqualTo(5693)
        }

    @Test
    fun `it decodes the scrobble lifecycle`() =
        runTest {
            val request = TraktScrobbleRequest(movie = TraktScrobbleMovie(ids = planNine), progress = 10f)
            val started = replay("scrobble/start", "scrobble_start.json") { TraktScrobbleApi(it).startWatching(request) }
            val paused = replay("scrobble/pause", "scrobble_pause.json") { TraktScrobbleApi(it).pauseWatching(request) }
            val stopped = replay("scrobble/stop", "scrobble_stop.json") { TraktScrobbleApi(it).stopWatching(request) }
            replay("sync/playback/1845766900", NO_CONTENT) { TraktSyncApi(it).removePlaybackProgress(1845766900) }

            assertThat(started.action).isEqualTo(TraktScrobbleAction.START)
            assertThat(paused.action).isEqualTo(TraktScrobbleAction.PAUSE)
            assertThat(stopped.action).isEqualTo(TraktScrobbleAction.PAUSE)
        }

    @Test
    fun `it decodes recommendations calendars and comment feeds of the signed-in user`() =
        runTest {
            val shows = replay("recommendations/shows?page=1&limit=3", "recommendations_shows.json") { TraktRecommendationsApi(it).getShows(page = 1, limit = 3) }
            replay("recommendations/movies/293990", NO_CONTENT) { TraktRecommendationsApi(it).hideMovie("293990") }
            val calendarShows = replay("calendars/my/shows/2026-09-12/7", "calendar_my_shows.json") { TraktCalendarsApi(it).getMyShows("2026-09-12", 7) }
            val calendarMovies = replay("calendars/my/movies/2026-09-12/30", "calendar_my_movies.json") { TraktCalendarsApi(it).getMyMovies("2026-09-12", 30) }
            val likes = replay("users/likes/comments?page=1&limit=10", "liked_comments.json") { TraktUsersApi(it).getLikes("comments", page = 1, limit = 10) }

            assertThat(shows).hasSize(3)
            assertThat(calendarShows.all { it.show != null }).isTrue()
            assertThat(calendarMovies.all { it.movie != null }).isTrue()
            assertThat(likes.size).isAtLeast(0)
        }

    @Test
    fun `it decodes the social graph of an account without followers`() =
        runTest {
            val followers = replay("users/me/followers", "followers.json") { TraktUsersApi(it).getFollowers(TraktUserSlug.ME) }
            val following = replay("users/me/following", "following.json") { TraktUsersApi(it).getFollowing(TraktUserSlug.ME) }
            val friends = replay("users/me/friends", "friends.json") { TraktUsersApi(it).getFriends(TraktUserSlug.ME) }

            assertThat(followers + following).isEmpty()
            assertThat(friends).isEmpty()
        }

    private suspend fun <T> replay(path: String, fixture: String, call: suspend (HttpClient) -> T): T {
        val file = if (fixture == NO_CONTENT) NO_CONTENT else "recorded/account/$fixture"
        return call(mockHttpClient(mapOf(path to file)))
    }
}
