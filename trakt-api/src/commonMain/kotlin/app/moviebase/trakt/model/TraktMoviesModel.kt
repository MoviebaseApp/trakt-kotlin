package app.moviebase.trakt.model

import kotlinx.serialization.Serializable

@Serializable
data class TraktMovie(
    val ids: TraktIds
)
