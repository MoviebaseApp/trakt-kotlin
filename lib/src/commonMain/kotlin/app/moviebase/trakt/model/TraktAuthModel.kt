package app.moviebase.trakt.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TraktGrantType(val value: String) {
    @SerialName("refresh_token")
    REFRESH_TOKEN("refresh_token"),

    @SerialName("authorization_code")
    AUTHORIZATION_CODE("authorization_code")
}

data class TraktAccessToken(
    @SerialName("access_token") val accessToken: String? = null,
    @SerialName("token_type") val tokenType: String? = null,
    @SerialName("expires_in") val expiresIn: Int? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
)

data class TraktTokenRefreshRequest(
    @SerialName("client_id") val clientId: String? = null,
    @SerialName("client_secret") val clientSecret: String? = null,
    @SerialName("redirect_uri") val redirectUri: String? = null,
    @SerialName("grant_type") val grantType: TraktGrantType? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
    @SerialName("code") val code: String? = null,
)
