package app.moviebase.trakt.api

import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktCheckinItem
import app.moviebase.trakt.remote.endPoint
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

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
