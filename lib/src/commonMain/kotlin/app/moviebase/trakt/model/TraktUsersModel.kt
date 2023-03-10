package app.moviebase.trakt.model

import kotlin.jvm.JvmInline
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@JvmInline
value class TraktUserSlug(val name: String) {
    init {
        // TODO: check here the name
    }

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
    @SerialName("vip") val vip: Boolean = false,
    @SerialName("joined_at") val joinedAt: Instant? = null,
    @SerialName("vip_ep") val vipEp: Boolean = false,
    @SerialName("ids") val ids: TraktUserIds? = null,
    @SerialName("vip_og") val vipOg: Boolean = false,
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

@Serializable
data class TraktUserStats(
    @SerialName("rating") val rating: Int,
)

@Serializable
data class TraktUserIds(
    @SerialName("slug") val slug: String,
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
data class TraktHistoryItem(
    @SerialName("id") val id: String? = null,
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("action") val action: String? = null,
    @SerialName("type") val type: TraktMediaType,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
)

@Serializable
data class TraktUserListItem(
    @SerialName("id") val id: Long,
    @SerialName("rank") val rank: Int,
    @SerialName("listed_at") val listedAt: Instant,
    @SerialName("type") val type: String,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
)

// TODO: Split into separate media classes (via sealed)?
@Serializable
data class TraktMediaItem(
    @SerialName("ids") val ids: TraktIds? = null,
    @SerialName("rating") val rating: Int? = null,
    @SerialName("type") val type: TraktMediaType? = null,

    @SerialName("seasons") val seasons: List<TraktSeason> = emptyList(),
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("season") val season: TraktSeason? = null,
    @SerialName("plays") val plays: Int = 0,

    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("last_collected_at") val lastCollectedAt: Instant? = null,
    @SerialName("last_watched_at") val lastWatchedAt: Instant? = null,
    @SerialName("last_updated_at") val lastUpdatedAt: Instant? = null,
    @SerialName("rated_at") val ratedAt: Instant? = null,
    @SerialName("listed_at") val listedAt: Instant? = null,
    @SerialName("hidden_at") val hiddenAt: Instant? = null,
)
