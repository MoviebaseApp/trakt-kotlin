package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktSyncItems(
    @SerialName("movies") var movies: List<TraktSyncMovie>? = null,
    @SerialName("shows") var shows: List<TraktSyncShow>? = null,
    @SerialName("episodes") var episodes: List<TraktSyncEpisode>? = null,
    @SerialName("people") var people: List<TraktSyncPerson>? = null,
    @SerialName("ids") var ids: List<Long>? = null,
)


sealed interface TraktSyncItem {
    val ids: TraktItemIds
    val rating: Int?
    val watchedAt: Instant?
    val collectedAt: Instant?
    val ratedAt: Instant?
}

@Serializable
data class TraktSyncMovie(
    @SerialName("ids") override val ids: TraktItemIds,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
) : TraktSyncItem

@Serializable
data class TraktSyncShow(
    @SerialName("ids") override val ids: TraktItemIds,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
    @SerialName("seasons") val seasons: List<TraktSyncSeason>? = null,
): TraktSyncItem

@Serializable
data class TraktSyncSeason(
    @SerialName("ids") override val ids: TraktItemIds,
    @SerialName("rating") override val rating: Int? = null,
    @SerialName("watched_at") override val watchedAt: Instant? = null,
    @SerialName("collected_at") override val collectedAt: Instant? = null,
    @SerialName("rated_at") override val ratedAt: Instant? = null,
    @SerialName("episodes") val episodes: List<TraktSyncEpisode> = emptyList(),
): TraktSyncItem

@Serializable
data class TraktSyncEpisode(
    @SerialName("ids") val ids: TraktItemIds,
    @SerialName("rating") val rating: Int? = null,
    @SerialName("watched_at") val watchedAt: Instant? = null,
    @SerialName("collected_at") val collectedAt: Instant? = null,
    @SerialName("rated_at") val ratedAt: Instant? = null,
)


@Serializable
data class TraktSyncPerson(
    @SerialName("ids") val ids: TraktPersonIds,
    @SerialName("name") val name: String? = null,
)

@Serializable
data class TraktSyncResponse(
    @SerialName("added") val added: TraktSyncStats? = null,
    @SerialName("existing") val existing: TraktSyncStats? = null,
    @SerialName("deleted") val deleted: TraktSyncStats? = null,
    @SerialName("not_found") val notFound: TraktSyncErrors? = null,
)

@Serializable
data class TraktSyncStats(
    @SerialName("movies") val movies: Int = 0,
    @SerialName("shows") val shows: Int = 0,
    @SerialName("seasons") val seasons: Int = 0,
    @SerialName("episodes") val episodes: Int = 0,
) {
    val count: Int get() = movies + shows + seasons + episodes
}

@Serializable
data class TraktSyncErrors(
    @SerialName("movies") val movies: List<TraktSyncMovie> = emptyList(),
    @SerialName("shows") val shows: List<TraktSyncShow> = emptyList(),
    @SerialName("seasons") val seasons: List<TraktSyncSeason> = emptyList(),
    @SerialName("episodes") val episodes: List<TraktSyncEpisode> = emptyList(),
    @SerialName("people") val people: List<TraktSyncPerson> = emptyList(),
    @SerialName("ids") val ids: List<Long> = emptyList(),
) {

    val isEmpty: Boolean get() = listOf(movies, shows, seasons, episodes, people, ids).all { it.isEmpty() }

}

