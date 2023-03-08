package app.moviebase.trakt.model

import app.moviebase.trakt.remote.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktEpisodeSummary(
    @SerialName("season") val season: Int,
    @SerialName("number") val number: Int,
    @SerialName("title") val title: String,
    @SerialName("ids") val ids: TraktIds? = null,
    @SerialName("first_aired")
    @Serializable(LocalDateTimeSerializer::class)
    val firstAired: LocalDateTime? = null, // date time in UTC
)

@Serializable
data class TraktEpisode(
    val season: Int,
    val number: Int,
    val title: String? = null,
    val ids: TraktIds,
    @SerialName("number_abs") val numberAbs: Int? = null,
    @SerialName("first_aired")
    @Serializable(LocalDateTimeSerializer::class)
    val firstAired: LocalDateTime? = null,
    val runtime: Int? = null,
    val tmdbNumber: Int? = null,
)
