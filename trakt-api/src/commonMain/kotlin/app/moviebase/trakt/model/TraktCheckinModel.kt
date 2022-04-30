package app.moviebase.trakt.model

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed class TraktCheckin {

    @Serializable
    data class Active(
        @SerialName("watched_at") val watchedAt: LocalDateTime?
    ) : TraktCheckin()

    @Serializable
    data class Error(
        @SerialName("expires_at") val expiresAt: LocalDateTime?
    ) : TraktCheckin()
}

@Serializable
data class TraktCheckinItem(
    val movie: TraktItem?,
    val show: TraktItem?,
    val episode: TraktEpisode?,
    val sharing: TraktSharing,
    val message: String?
)

@Serializable
data class TraktSharing(
    val facebook: Boolean,
    val twitter: Boolean,
    val tumblr: Boolean
)
