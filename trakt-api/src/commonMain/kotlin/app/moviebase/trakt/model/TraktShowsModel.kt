package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktShow(
    val title: String,
    val year: Int,
    val ids: TraktIds,
    val network: String? = null,
    @SerialName("aired_episodes") val airedEpisodes: Int? = null
)
