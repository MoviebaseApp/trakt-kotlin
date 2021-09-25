package app.moviebase.trakt.model

import app.moviebase.trakt.remote.LocalDateTimeSerializer
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktEpisode(
    val season: Int,
    val number: Int,
    val title: String,
    val ids: TraktIds,
    @SerialName("number_abs") val numberAbs: Int?,
    @SerialName("first_aired") @Serializable(LocalDateTimeSerializer::class) val firstAired: LocalDateTime? = null,
    val runtime: Int,
    val tmdbNumber: Int? = null
)
