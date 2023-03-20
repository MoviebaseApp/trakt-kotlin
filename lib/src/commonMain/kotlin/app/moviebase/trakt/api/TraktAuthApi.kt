package app.moviebase.trakt.api

import app.moviebase.trakt.TraktClientConfig
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktAccessToken
import app.moviebase.trakt.model.TraktGrantType
import app.moviebase.trakt.model.TraktTokenRefreshRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktAuthApi(
    private val client: HttpClient,
    private val config: TraktClientConfig,
) {

    suspend fun postToken(request: TraktTokenRefreshRequest): TraktAccessToken = client.postByPaths("oauth", "token") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }

    suspend fun requestAccessToken(
        redirectUri: String,
        code: String,
    ): TraktAccessToken {
        val requestToken = TraktTokenRefreshRequest(
            clientId = config.traktApiKey,
            clientSecret = config.clientSecret,
            redirectUri = redirectUri,
            grantType = TraktGrantType.AUTHORIZATION_CODE,
            code = code,
        )
        return postToken(requestToken)
    }

    suspend fun requestRefreshToken(
        redirectUri: String,
        refreshToken: String,
    ): TraktAccessToken {
        require(refreshToken.isNotBlank()) { "refresh token is empty" }

        val requestToken = TraktTokenRefreshRequest(
            clientId = config.traktApiKey,
            clientSecret = config.clientSecret,
            redirectUri = redirectUri,
            grantType = TraktGrantType.REFRESH_TOKEN,
            refreshToken = refreshToken,
        )
        return postToken(requestToken)
    }
}
