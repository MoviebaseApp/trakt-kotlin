package app.moviebase.trakt.api

import app.moviebase.trakt.core.deleteByPaths
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktCheckinItem
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class TraktCheckinApi(private val client: HttpClient) {

    suspend fun postCheckin(item: TraktCheckinItem): TraktCheckin.Active = client.postByPaths("checkin") {
        setBody(item)
    }

    suspend fun deleteCheckin(): TraktCheckin.Active = client.deleteByPaths("checkin")
}
