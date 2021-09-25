package app.moviebase.trakt.remote

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.request.*
import io.ktor.http.*

fun HttpRequestBuilder.json() {
    contentType(ContentType.Application.Json)
}

fun HttpRequestBuilder.endPoint(vararg paths: String) {
    url {
        takeFrom(TraktWebConfig.BASE_URL)
        path(*paths)
    }
}

fun HttpRequestBuilder.parameters(parameters: Map<String, Any?>) {
    parameters.entries.forEach {
        parameter(it.key, it.value)
    }
}

fun HttpRequestBuilder.parameterExtended(extended: String = TraktExtended.FULL) {
    parameter("extended", extended)
}

