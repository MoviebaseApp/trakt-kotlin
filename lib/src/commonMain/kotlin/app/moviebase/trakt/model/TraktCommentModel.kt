package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktPostComment(
    @SerialName("comment") val comment: String,
    @SerialName("spoiler") val spoiler: Boolean,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("season") val season: TraktSeason? = null,
)

@Serializable
data class TraktComment(
    @SerialName("id") val id: Int,
    @SerialName("created_at") val createdAt: Instant? = null,
    @SerialName("comment") val comment: String,
    @SerialName("spoiler") val spoiler: Boolean,
    @SerialName("likes") val likes: Int? = null,
    @SerialName("replies") val replies: Int? = null,
    @SerialName("user") val user: TraktUser?,
    @SerialName("user_stats") val userStats: TraktUserStats? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("season") val season: TraktSeason? = null,
) {

    val displayUserName get() = user?.name ?: user?.userName
    val imagePath get() = user?.imagePath
    val containsSpoiler get() = comment.contains("[spoiler]")
}
