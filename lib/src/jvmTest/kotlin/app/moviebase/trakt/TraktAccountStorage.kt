package app.moviebase.trakt

import io.ktor.client.plugins.auth.providers.BearerTokens

class TraktAccountStorage {
    var bearerTokens: BearerTokens? = null
}
