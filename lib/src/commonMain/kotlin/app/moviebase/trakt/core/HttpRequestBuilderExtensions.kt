package app.moviebase.trakt.core

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.path

fun HttpRequestBuilder.json() {
    contentType(ContentType.Application.Json)
}

fun HttpRequestBuilder.endPoint(vararg paths: String) {
    url {
        url(TraktWebConfig.BASE_URL)
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
