package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktShow(
    val title: String? = null,
    val year: Int? = null,
    val ids: TraktItemIds,
    val network: String? = null,
    @SerialName("aired_episodes") val airedEpisodes: Int? = null,
    val runtime: Int? = null,
)

data class TraktAirs(
    @SerialName("day") val day: String,
    @SerialName("time") val time: String,
    @SerialName("timezone") val timezone: String,
)

object TraktShowStatus {
    const val STATUS_TEXT_RETURNING_SERIES = "returning series" // airing soon
    const val STATUS_TEXT_IN_PRODUCTION = "in production" // airing soon
    const val STATUS_TEXT_PLANNED = "planned" // (in development)
    const val STATUS_TEXT_CANCELED = "canceled" // (in development)
    const val STATUS_TEXT_ENDED = "ended"
}
