package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktCheckinItem
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class TraktCheckinApi(private val client: HttpClient) {

    suspend fun postCheckin(item: TraktCheckinItem): TraktCheckin.Active =
        client.post {
            endPointCheckin()
            setBody(item)
        }.body()

    suspend fun deleteCheckin(): TraktCheckin.Active =
        client.delete {
            endPointCheckin()
        }.body()

    private fun HttpRequestBuilder.endPointCheckin(vararg paths: String) {
        endPoint("checkin", *paths)
    }
}
