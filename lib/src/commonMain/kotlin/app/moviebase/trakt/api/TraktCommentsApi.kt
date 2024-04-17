package app.moviebase.trakt.api

import app.moviebase.trakt.core.buildPaths
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktPostComment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktCommentsApi(private val client: HttpClient) {

    suspend fun postComment(comment: TraktPostComment): TraktCheckin.Active =
        client.postByPaths("comments") {
            contentType(ContentType.Application.Json)
            setBody(comment)
        }

    suspend fun updateComment(
        id: Int,
        comment: TraktPostComment,
    ): TraktComment = client.put(urlString = buildPaths("comments", id.toString())) {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }.body()

    suspend fun getComment(id: Int): TraktComment = client.getByPaths("comments", id.toString())

    suspend fun deleteComment(id: Int): TraktComment =
        client.delete(urlString = buildPaths("comments", id.toString())).body()

    suspend fun getCommentReplies(id: Int): List<TraktComment> =
        client.getByPaths("comments", id.toString(), "replies")

    suspend fun postCommentReplies(
        id: Int,
        comment: TraktPostComment,
    ): List<TraktComment> = client.postByPaths("comments", id.toString(), "replies") {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }
}
