package app.moviebase.trakt.api

import app.moviebase.trakt.core.endPoint
import app.moviebase.trakt.core.parameterLimit
import app.moviebase.trakt.core.parameterPage
import app.moviebase.trakt.model.TraktComment
import app.moviebase.trakt.model.TraktCommentItem
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktPostComment
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TraktCommentsApi(
    private val client: HttpClient,
) {
    suspend fun postComment(comment: TraktPostComment): TraktComment = client.post {
        endPoint("comments")
        contentType(ContentType.Application.Json)
        setBody(comment)
    }.body()

    suspend fun updateComment(
        id: Int,
        comment: TraktPostComment,
    ): TraktComment = client.put {
        endPoint("comments", id.toString())
        contentType(ContentType.Application.Json)
        setBody(comment)
    }.body()

    suspend fun getComment(id: Int): TraktComment = client.get {
        endPoint("comments", id.toString())
    }.body()

    /**
     * Returns the media item attached to a comment.
     */
    suspend fun getCommentItem(id: Int): TraktCommentItem = client.get {
        endPoint("comments", id.toString(), "item")
    }.body()

    suspend fun deleteComment(id: Int) {
        client.delete {
            endPoint("comments", id.toString())
        }
    }

    suspend fun getCommentReplies(id: Int): List<TraktComment> = client.get {
        endPoint("comments", id.toString(), "replies")
    }.body()

    suspend fun postCommentReplies(
        id: Int,
        comment: TraktPostComment,
    ): TraktComment = client.post {
        endPoint("comments", id.toString(), "replies")
        contentType(ContentType.Application.Json)
        setBody(comment)
    }.body()

    suspend fun likeComment(id: Int) = client.post {
        endPoint("comments", id.toString(), "like")
    }

    suspend fun unlikeComment(id: Int) = client.delete {
        endPoint("comments", id.toString(), "like")
    }

    suspend fun getTrending(
        commentType: String? = null,
        type: TraktMediaType? = null,
        includeReplies: Boolean? = null,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktComment> = client.get {
        endPointComments("trending", commentType, type)
        includeReplies?.let { parameter("include_replies", it) }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getRecent(
        commentType: String? = null,
        type: TraktMediaType? = null,
        includeReplies: Boolean? = null,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktComment> = client.get {
        endPointComments("recent", commentType, type)
        includeReplies?.let { parameter("include_replies", it) }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    suspend fun getUpdates(
        commentType: String? = null,
        type: TraktMediaType? = null,
        includeReplies: Boolean? = null,
        page: Int = 1,
        limit: Int = 10,
    ): List<TraktComment> = client.get {
        endPointComments("updates", commentType, type)
        includeReplies?.let { parameter("include_replies", it) }
        parameterPage(page)
        parameterLimit(limit)
    }.body()

    private fun HttpRequestBuilder.endPointComments(
        category: String,
        commentType: String?,
        type: TraktMediaType?,
    ) {
        val paths = buildList {
            add("comments")
            add(category)
            commentType?.let { add(it) }
            type?.let { add(it.value) }
        }
        endPoint(*paths.toTypedArray())
    }
}
