// ktlint-disable filename

package app.moviebase.trakt.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktPerson(
    @SerialName("name") val name: String,
    @SerialName("ids") val ids: TraktPersonIds,
    @SerialName("biography") val biography: String? = null,
    @SerialName("birthday") val birthday: String? = null,
    @SerialName("death") val death: LocalDate? = null,
    @SerialName("birthplace") val birthplace: String? = null,
    @SerialName("homepage") val homepage: String? = null,
)
