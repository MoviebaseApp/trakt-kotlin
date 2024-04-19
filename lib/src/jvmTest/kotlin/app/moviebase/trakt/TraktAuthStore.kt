package app.moviebase.trakt

import io.ktor.client.plugins.auth.providers.BearerTokens

class TraktAuthStore {
    var bearerTokens: BearerTokens? = null
}
