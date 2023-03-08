package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TraktMediaType(val value: String) {
    @SerialName("movie")
    MOVIE("movie"),

    @SerialName("show")
    SHOW("show"),

    @SerialName("season")
    SEASON("season"),

    @SerialName("episode")
    EPISODE("episode")
}

@Serializable
data class TraktIds(
    val trakt: Int,
    val slug: String? = null,
    val tmdb: Int? = null,
    val tvdb: Int? = null,
    val imdb: String? = null
)
