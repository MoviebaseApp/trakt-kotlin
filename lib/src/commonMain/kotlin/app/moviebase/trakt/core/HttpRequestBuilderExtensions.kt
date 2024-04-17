package app.moviebase.trakt.core

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.TraktWebConfig
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.parameter
import io.ktor.http.path
import io.ktor.http.takeFrom
import kotlinx.datetime.Instant

fun HttpRequestBuilder.endPoint(vararg paths: String) {
    url {
        takeFrom(TraktWebConfig.BASE_URL)
        path(*paths)
    }
}

fun HttpRequestBuilder.parameters(parameters: Map<String, Any?>) {
    parameters.entries.forEach {
        if (it.value != null) parameter(it.key, it.value)
    }
}

fun HttpRequestBuilder.parameterPage(page: Int) {
    parameter("page", page)
}

fun HttpRequestBuilder.parameterLimit(limit: Int) {
    parameter("limit", limit)
}

fun HttpRequestBuilder.parameterExtended(extended: TraktExtended = TraktExtended.FULL) {
    parameter("extended", extended.value)
}

fun HttpRequestBuilder.parameterStartAt(startAt: Instant) {
    parameter("start_at", startAt.toString())
}

fun HttpRequestBuilder.parameterEndAt(endAt: Instant) {
    parameter("end_at", endAt.toString())
}
