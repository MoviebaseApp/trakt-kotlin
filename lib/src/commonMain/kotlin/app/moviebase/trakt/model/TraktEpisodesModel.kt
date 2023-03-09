package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TODO: Check if we need both classes
@Serializable
data class TraktEpisodeSummary(
    @SerialName("season") val season: Int,
    @SerialName("number") val number: Int,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String? = null,
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("first_aired") val firstAired: Instant? = null,
    @SerialName("number_abs") val numberAbs: Int? = null,
    @SerialName("rating") val rating: Int? = null,
    @SerialName("votes") val votes: Int? = null,
)

@Serializable
data class TraktEpisode(
    @SerialName("season") val season: Int,
    @SerialName("number") val number: Int,
    @SerialName("title") val title: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("number_abs") val numberAbs: Int? = null,
    @SerialName("first_aired") val firstAired: Instant? = null,
    @SerialName("rating") val rating: Float? = null,
    @SerialName("votes") val votes: Int? = null,
)
