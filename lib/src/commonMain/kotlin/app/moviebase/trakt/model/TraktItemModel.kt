package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktItem(
    @SerialName("title") val title: String?,
    @SerialName("ids") val ids: TraktIds
)
