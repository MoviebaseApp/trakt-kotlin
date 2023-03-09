package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TraktListCategory {
    const val TRENDING = "trending"
    const val ANTICIPATED = "anticipated"
    const val BOX_OFFICE = "boxoffice"
}

@Serializable
enum class TraktListType(val value: String) {
    @SerialName("movies")
    MOVIES("movies"),

    @SerialName("shows")
    SHOWS("shows"),

    @SerialName("seasons")
    SEASONS("seasons"),

    @SerialName("episodes")
    EPISODES("episodes")
}

@Serializable
enum class TraktListPrivacy {
    @SerialName("private")
    PRIVATE,

    @SerialName("friends")
    FRIENDS,

    @SerialName("public")
    PUBLIC
}


@Serializable
data class TraktListItem(
    @SerialName("id") val id: Long,
    @SerialName("rank") val rank: Int,
    @SerialName("listed_at") val listedAt: Instant,
    @SerialName("type") val type: String,
    @SerialName("movie") val movie: TraktMovie? = null,
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("episode") val episode: TraktEpisode? = null,
    @SerialName("person") val person: TraktPerson? = null,
)
