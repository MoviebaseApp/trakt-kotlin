package app.moviebase.trakt.model

import kotlin.jvm.JvmInline
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
value class TraktUserSlug(
    val name: String,
) {

    companion object {
        val ME = TraktUserSlug("me")
    }
}

@Serializable
data class TraktUserSettings(
    @SerialName("user") val user: TraktUser,
    @SerialName("account") val account: TraktAccount,
)

/**
 * VIP: If a user is a regular VIP.
 * VIP_EP: If a user is an execute producer.
 */
@Serializable
data class TraktUser(
    @SerialName("username") val userName: String? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("location") val location: String? = null,
    @SerialName("about") val about: String? = null,
    @SerialName("gender") val gender: String? = null,
    @SerialName("age") val age: Int? = null,
    @SerialName("private") val private: Boolean = false,
    @SerialName("director") val director: Boolean = false,
    @SerialName("vip") val vip: Boolean = false,
    @SerialName("joined_at") val joinedAt: Instant? = null,
    @SerialName("vip_ep") val vipEp: Boolean = false,
    @SerialName("ids") val ids: TraktUserIds? = null,
    @SerialName("vip_og") val vipOg: Boolean = false,
    @SerialName("vip_years") val vipYears: Int = 0,
    @SerialName("vip_cover_image") val vipCoverImage: String? = null,
    @SerialName("images") val images: TraktUserImage? = null,
) {
    val imagePath get() = images?.avatar?.full
    val userId get() = ids?.slug
}

@Serializable
data class TraktAccount(
    @SerialName("timezone") val timezone: String? = null,
    @SerialName("cover_image") val coverImage: String? = null,
)

/**
 * Response from users/{id}/stats.
 */
@Serializable
data class TraktUserStats(
    @SerialName("movies") val movies: TraktMovieStats = TraktMovieStats(),
    @SerialName("shows") val shows: TraktShowStats = TraktShowStats(),
    @SerialName("seasons") val seasons: TraktSeasonStats = TraktSeasonStats(),
    @SerialName("episodes") val episodes: TraktEpisodeStats = TraktEpisodeStats(),
    @SerialName("network") val network: TraktNetworkStats = TraktNetworkStats(),
    @SerialName("ratings") val ratings: TraktRatingStats = TraktRatingStats(),
)

@Serializable
data class TraktMovieStats(
    @SerialName("plays") val plays: Int = 0,
    @SerialName("watched") val watched: Int = 0,
    @SerialName("minutes") val minutes: Int = 0,
    @SerialName("collected") val collected: Int = 0,
    @SerialName("ratings") val ratings: Int = 0,
    @SerialName("comments") val comments: Int = 0,
)

@Serializable
data class TraktShowStats(
    @SerialName("watched") val watched: Int = 0,
    @SerialName("collected") val collected: Int = 0,
    @SerialName("ratings") val ratings: Int = 0,
    @SerialName("comments") val comments: Int = 0,
)

@Serializable
data class TraktSeasonStats(
    @SerialName("ratings") val ratings: Int = 0,
    @SerialName("comments") val comments: Int = 0,
)

@Serializable
data class TraktEpisodeStats(
    @SerialName("plays") val plays: Int = 0,
    @SerialName("watched") val watched: Int = 0,
    @SerialName("minutes") val minutes: Int = 0,
    @SerialName("collected") val collected: Int = 0,
    @SerialName("ratings") val ratings: Int = 0,
    @SerialName("comments") val comments: Int = 0,
)

@Serializable
data class TraktNetworkStats(
    @SerialName("friends") val friends: Int = 0,
    @SerialName("followers") val followers: Int = 0,
    @SerialName("following") val following: Int = 0,
)

@Serializable
data class TraktRatingStats(
    @SerialName("total") val total: Int = 0,
    @SerialName("distribution") val distribution: Map<String, Float> = emptyMap(),
)

@Serializable
data class TraktUserIds(
    @SerialName("slug") val slug: String,
    @SerialName("trakt") val trakt: Int? = null,
    @SerialName("uuid") val uuid: String? = null,
)

@Serializable
data class TraktUserImage(
    @SerialName("avatar") val avatar: TraktAvatar? = null,
)

@Serializable
data class TraktAvatar(
    @SerialName("full") val full: String? = null,
)

@Serializable
data class TraktList(
    @SerialName("name") val name: String? = null,
    @SerialName("ids") val ids: TraktListIds? = null,
    @SerialName("privacy") val privacy: TraktListPrivacy? = null,
)

@Serializable
data class TraktListIds(
    @SerialName("trakt") val trakt: Int? = null,
    @SerialName("slug") val slug: String? = null,
)

@Serializable
data class TraktReorderRequest(
    @SerialName("rank") val rank: List<Long>,
)

@Serializable
data class TraktReorderResponse(
    @SerialName("updated") val updated: Int = 0,
    @SerialName("skipped_ids") val skippedIds: List<Long> = emptyList(),
    @SerialName("list") val list: TraktReorderedList? = null,
)

@Serializable
data class TraktReorderedList(
    @SerialName("updated_at") val updatedAt: Instant? = null,
    @SerialName("item_count") val itemCount: Int? = null,
)

@Serializable
data class TraktHistoryItem(
    @SerialName("id") val id: String? = null,
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("action") val action: String? = null,
    @SerialName("type") val type: TraktMediaType,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
)

/**
 * Response from users/{id}/watching endpoint.
 * Returns what a user is currently watching. If they are not watching anything, the API returns 204 No Content.
 */
@Serializable
data class TraktWatching(
    @SerialName("expires_at") val expiresAt: Instant? = null,
    @SerialName("started_at") val startedAt: Instant? = null,
    @SerialName("action") val action: String? = null,
    @SerialName("type") val type: TraktMediaType,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
)

/**
 * Item types for user list items. Includes person in addition to media types.
 */
@Serializable
enum class TraktListItemType(
    val value: String,
) {
    @SerialName("movie")
    MOVIE("movie"),

    @SerialName("show")
    SHOW("show"),

    @SerialName("season")
    SEASON("season"),

    @SerialName("episode")
    EPISODE("episode"),

    @SerialName("person")
    PERSON("person"),
}

@Serializable
data class TraktUserListItem(
    @SerialName("id") val id: Long,
    @SerialName("rank") val rank: Int,
    @SerialName("listed_at") val listedAt: Instant,
    @SerialName("type") val type: TraktListItemType? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
)

@Serializable
data class TraktFollower(
    @SerialName("followed_at") val followedAt: Instant? = null,
    @SerialName("user") val user: TraktUser? = null,
)

@Serializable
data class TraktFollowResponse(
    @SerialName("approved_at") val approvedAt: Instant? = null,
    @SerialName("user") val user: TraktUser? = null,
)

@Serializable
data class TraktFollowRequest(
    @SerialName("id") val id: Int,
    @SerialName("requested_at") val requestedAt: Instant,
    @SerialName("user") val user: TraktUser,
)

@Serializable
data class TraktLike(
    @SerialName("liked_at") val likedAt: Instant,
    @SerialName("type") val type: String,
    @SerialName("list") val list: TraktList? = null,
    @SerialName("comment") val comment: TraktComment? = null,
)

/**
 * Common interface for media items returned from sync and user endpoints.
 * All implementations contain at least movie and show fields.
 */
sealed interface TraktMediaItem {
    val movie: TraktMovie?
    val show: TraktShow?
}

/**
 * Response item from sync/watched/movies and sync/watched/shows endpoints.
 */
@Serializable
data class TraktWatchedItem(
    @SerialName("plays") val plays: Int = 0,
    @SerialName("last_watched_at") val lastWatchedAt: Instant? = null,
    @SerialName("last_updated_at") val lastUpdatedAt: Instant? = null,
    @SerialName("reset_at") val resetAt: Instant? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("seasons") val seasons: List<TraktWatchedSeason> = emptyList(),
) : TraktMediaItem

/**
 * Season data from sync/watched/shows endpoint.
 * Contains only the season number and list of watched episodes.
 */
@Serializable
data class TraktWatchedSeason(
    @SerialName("number") val number: Int,
    @SerialName("episodes") val episodes: List<TraktWatchedEpisode> = emptyList(),
)

/**
 * Episode data from sync/watched/shows endpoint.
 * Contains the episode number, play count, and last watched timestamp.
 */
@Serializable
data class TraktWatchedEpisode(
    @SerialName("number") val number: Int,
    @SerialName("plays") val plays: Int = 0,
    @SerialName("last_watched_at") val lastWatchedAt: Instant? = null,
)

/**
 * Response item from sync/collection/movies and sync/collection/shows endpoints.
 */
@Serializable
data class TraktCollectionItem(
    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("last_collected_at") val lastCollectedAt: Instant? = null,
    @SerialName("last_updated_at") val lastUpdatedAt: Instant? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("seasons") val seasons: List<TraktSeason> = emptyList(),
) : TraktMediaItem

/**
 * Response item from sync/watchlist endpoints.
 */
@Serializable
data class TraktWatchlistItem(
    @SerialName("rank") val rank: Int = 0,
    @SerialName("id") val id: Long = 0,
    @SerialName("listed_at") val listedAt: Instant? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("type") val type: TraktMediaType? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
) : TraktMediaItem

/**
 * Response item from sync/ratings endpoints.
 */
@Serializable
data class TraktRatedItem(
    @SerialName("rating") val rating: Int = 0,
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("type") val type: TraktMediaType? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
) : TraktMediaItem

/**
 * Hidden section types for users/hidden/{section} endpoint.
 */
@Serializable
enum class TraktHiddenSection(
    val value: String,
) {
    @SerialName("calendar")
    CALENDAR("calendar"),

    @SerialName("progress_watched")
    PROGRESS_WATCHED("progress_watched"),

    @SerialName("progress_watched_reset")
    PROGRESS_WATCHED_RESET("progress_watched_reset"),

    @SerialName("progress_collected")
    PROGRESS_COLLECTED("progress_collected"),

    @SerialName("recommendations")
    RECOMMENDATIONS("recommendations"),

    @SerialName("comments")
    COMMENTS("comments"),
}

/**
 * Response item from users/hidden endpoints.
 */
@Serializable
data class TraktHiddenItem(
    @SerialName("hidden_at") val hiddenAt: Instant? = null,
    @SerialName("type") val type: TraktMediaType? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
) : TraktMediaItem

/**
 * Response item from users/favorites endpoints.
 */
@Serializable
data class TraktFavoriteItem(
    @SerialName("rank") val rank: Int = 0,
    @SerialName("id") val id: Long = 0,
    @SerialName("listed_at") val listedAt: Instant? = null,
    @SerialName("notes") val notes: String? = null,
    @SerialName("type") val type: TraktMediaType? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
) : TraktMediaItem

/**
 * Response item from comments/:id/item endpoint.
 * Returns the media item a comment is attached to.
 */
@Serializable
data class TraktCommentItem(
    @SerialName("type") val type: TraktMediaType? = null,
    @SerialName("movie") override val movie: TraktMovie? = null,
    @SerialName("show") override val show: TraktShow? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("list") val list: TraktList? = null,
    @SerialName("comment") val comment: TraktComment? = null,
) : TraktMediaItem
