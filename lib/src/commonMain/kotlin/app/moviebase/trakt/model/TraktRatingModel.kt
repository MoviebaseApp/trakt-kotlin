// ktlint-disable filename

package app.moviebase.trakt.model

import kotlinx.serialization.Serializable

@Serializable
data class TraktRating(
    val rating: Double,
    val votes: Int,
)
