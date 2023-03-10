package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TraktMediaType(internal val type: String, internal val path: String) {
    @SerialName("movie")
    MOVIE(type = "movie", path = "movies"),

    @SerialName("show")
    SHOW(type = "show", path = "shows"),

    @SerialName("season")
    SEASON(type = "season", path = "seasons"),

    @SerialName("episode")
    EPISODE(type = "episode", path = "episodes"),
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
