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

sealed interface TraktIds {
     val trakt: Int?
     val slug: String?
     val tmdb: Int?
     val imdb: String?
}

@Serializable
data class TraktItemIds(
    @SerialName("trakt") override val trakt: Int? = null,
    @SerialName("slug") override val slug: String? = null,
    @SerialName("tmdb") override val tmdb: Int? = null,
    @SerialName("tvdb") val tvdb: Int? = null,
    @SerialName("imdb") override val imdb: String? = null,
) : TraktIds

@Serializable
data class TraktPersonIds(
    @SerialName("trakt") override val trakt: Int,
    @SerialName("slug") override val slug: String? = null,
    @SerialName("tmdb") override val tmdb: Int? = null,
    @SerialName("imdb") override val imdb: String? = null,
    @SerialName("tvrage") val tvrage: String? = null,
) : TraktIds
