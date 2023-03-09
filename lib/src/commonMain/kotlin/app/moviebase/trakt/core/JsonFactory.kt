package app.moviebase.trakt.core

import kotlinx.serialization.json.Json

internal object JsonFactory {

    fun create(): Json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
        isLenient = true
        allowSpecialFloatingPointValues = true
        prettyPrint = false
    }
}
