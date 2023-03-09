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
    EPISODE("episode"),
}

@Serializable
data class TraktIds(
    @SerialName("trakt") val trakt: Int,
    @SerialName("slug") val slug: String? = null,
    @SerialName("tmdb") val tmdb: Int? = null,
    @SerialName("tvdb") val tvdb: Int? = null,
    @SerialName("imdb") val imdb: String? = null,
)
