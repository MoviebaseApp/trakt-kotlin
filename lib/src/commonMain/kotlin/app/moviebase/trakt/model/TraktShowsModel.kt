package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktShow(
    @SerialName("title") val title: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("certification") val certification: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("network") val network: String? = null,
    @SerialName("aired_episodes") val airedEpisodes: Int? = null,
    @SerialName("first_aired") val firstAired: Instant? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("rating") val rating: Float? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("status") val status: TraktShowStatus? = null,
    @SerialName("airs") val airs: TraktAirs? = null,
    @SerialName("genres") val genres: List<String> = emptyList(),
)

@Serializable
data class TraktAirs(
    @SerialName("day") val day: String? = null,
    @SerialName("time") val time: String? = null,
    @SerialName("timezone") val timezone: String? = null,
)

@Serializable
enum class TraktShowStatus(val value: String) {
    @SerialName("returning series")
    RETURNING_SERIES("returning series"),

    @SerialName("in production")
    IN_PRODUCTION("in production"),

    @SerialName("planned")
    PLANNED("planned"),

    @SerialName("canceled")
    CANCELED("canceled"),

    @SerialName("ended")
    ENDED("ended"),
}

@Serializable
data class TraktTrendingShow(
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("watchers") val watchers: Int? = null,
)

@Serializable
data class TraktAnticipatedShow(
    @SerialName("show") val show: TraktShow? = null,
    @SerialName("list_count") val listCount: Int? = null,
)
