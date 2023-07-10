// ktlint-disable filename

package app.moviebase.trakt.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktSeason(
    @SerialName("number") val number: Int,
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("rating") val rating: Float? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("episode_count") val episodeCount: Int? = null,
    @SerialName("aired_episodes") val airedEpisodes: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("network") val network: String? = null,
    @SerialName("first_aired") val firstAired: Instant? = null,
    @SerialName("updated_at") val updatedAt: Instant? = null,
    @SerialName("episodes") val episodes: List<TraktEpisode>? = null,
)
