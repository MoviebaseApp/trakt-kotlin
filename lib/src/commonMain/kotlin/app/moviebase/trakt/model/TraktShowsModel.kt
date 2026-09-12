package app.moviebase.trakt.model

import kotlin.time.Instant
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
enum class TraktShowStatus(
    val value: String,
) {
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
data class TraktShowCounts(
    @SerialName("watcher_count") val watcherCount: Int? = null,
    @SerialName("play_count") val playCount: Int? = null,
    @SerialName("collected_count") val collectedCount: Int? = null,
    @SerialName("collector_count") val collectorCount: Int? = null,
    @SerialName("show") val show: TraktShow? = null,
)

@Serializable
data class TraktShowCertification(
    @SerialName("certification") val certification: String? = null,
    @SerialName("country") val country: String? = null,
)

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

@Serializable
data class TraktShowUpdate(
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("show") val show: TraktShow,
)

@Serializable
data class TraktShowProgress(
    @SerialName("aired") val aired: Int = 0,
    @SerialName("completed") val completed: Int = 0,
    @SerialName("last_watched_at") val lastWatchedAt: Instant? = null,
    @SerialName("reset_at") val resetAt: Instant? = null,
    @SerialName("seasons") val seasons: List<TraktSeasonProgress> = emptyList(),
    @SerialName("hidden_seasons") val hiddenSeasons: List<TraktSeason> = emptyList(),
    @SerialName("next_episode") val nextEpisode: TraktEpisode? = null,
    @SerialName("last_episode") val lastEpisode: TraktEpisode? = null,
)

@Serializable
data class TraktSeasonProgress(
    @SerialName("number") val number: Int,
    @SerialName("title") val title: String? = null,
    @SerialName("aired") val aired: Int = 0,
    @SerialName("completed") val completed: Int = 0,
    @SerialName("episodes") val episodes: List<TraktEpisodeProgress> = emptyList(),
)

@Serializable
data class TraktEpisodeProgress(
    @SerialName("number") val number: Int,
    @SerialName("completed") val completed: Boolean = false,
    @SerialName("last_watched_at") val lastWatchedAt: Instant? = null,
)
