package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TraktListCategory {
    const val TRENDING = "trending"
    const val ANTICIPATED = "anticipated"
    const val BOX_OFFICE = "boxoffice"
}

@Serializable
enum class TraktListMediaType(val value: String) {
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
enum class TraktListType(val value: String) {
    @SerialName("collection")
    COLLECTION("collection"),

    @SerialName("ratings")
    RATINGS("ratings"),

    @SerialName("watchlist")
    WATCHLIST("watchlist"),

    @SerialName("watched")
    WATCHED("watched")
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
