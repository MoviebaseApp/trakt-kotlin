package app.moviebase.trakt.model

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
