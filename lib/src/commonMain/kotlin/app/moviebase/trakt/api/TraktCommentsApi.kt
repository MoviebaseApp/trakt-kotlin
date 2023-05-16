package app.moviebase.trakt.api

import app.moviebase.trakt.core.deleteByPaths
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.postByPaths
import app.moviebase.trakt.core.putByPaths
import app.moviebase.trakt.model.TraktCheckin
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktPostComment
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktCommentsApi(private val client: HttpClient) {

    suspend fun postComment(comment: TraktPostComment): TraktCheckin.Active = client.postByPaths("comments") {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }

    suspend fun updateComment(id: Int, comment: TraktPostComment): TraktComment = client.putByPaths("comments", id.toString()) {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }

    suspend fun getComment(id: Int): TraktComment = client.getByPaths("comments", id.toString())

    suspend fun deleteComment(id: Int): TraktComment = client.deleteByPaths("comments", id.toString())

    suspend fun getCommentReplies(id: Int): List<TraktComment> = client.getByPaths("comments", id.toString(), "replies")

    suspend fun postCommentReplies(
        id: Int,
        comment: TraktPostComment,
    ): List<TraktComment> = client.postByPaths("comments", id.toString(), "replies") {
        contentType(ContentType.Application.Json)
        setBody(comment)
    }
}
