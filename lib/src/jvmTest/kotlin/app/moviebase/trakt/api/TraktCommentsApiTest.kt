package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
import app.moviebase.trakt.model.TraktMediaType
import app.moviebase.trakt.model.TraktPostComment
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class TraktCommentsApiTest {
    val client =
        mockHttpClient(
            responses =
                mapOf(
                    "comments/1035361" to "comments/comment.json",
                    "comments/1035361/replies" to "comments/replies.json",
                ),
        )

    val classToTest = TraktCommentsApi(client)

    @Test
    fun `it can fetch a comment`() =
        runTest {
            val comment = classToTest.getComment(1035361)

            assertThat(comment.id).isEqualTo(1035361)
            assertThat(comment.parentId).isEqualTo(0)
            assertThat(comment.replies).isEqualTo(2)
            assertThat(comment.user?.ids?.slug).isEqualTo("dinismiguel")
        }

    @Test
    fun `it can fetch the replies of a comment`() =
        runTest {
            val replies = classToTest.getCommentReplies(1035361)

            assertThat(replies).hasSize(2)
            assertThat(replies.map { it.parentId }).containsExactly(1035361, 1035361)
        }

    @Test
    fun `it decodes the trending feed with the item each comment belongs to`() =
        runTest {
            val feedClient = mockHttpClient(mapOf("comments/trending?page=1&limit=10" to "comments/trending.json"))

            val feed = TraktCommentsApi(feedClient).getTrending()

            assertThat(feed.map { it.comment?.id }).containsExactly(665578, 1041365, 1041419).inOrder()
            assertThat(feed[0].list).isNotNull()
            assertThat(feed[1].type).isEqualTo(TraktMediaType.EPISODE)
            assertThat(feed[1].show).isNotNull()
        }

    @Test
    fun `it filters the trending feed by media type`() =
        runTest {
            val feedClient = mockHttpClient(mapOf("comments/trending/all/movies?page=1&limit=10" to "comments/trending_movies.json"))

            val feed = TraktCommentsApi(feedClient).getTrending(type = TraktMediaType.MOVIE)

            assertThat(feed.map { it.type }.toSet()).containsExactly(TraktMediaType.MOVIE)
            assertThat(feed.first().movie?.title).isEqualTo("Project Hail Mary")
            assertThat(feed.first().comment?.id).isEqualTo(935888)
        }

    @Test
    fun `posting a reply returns the created reply`() =
        runTest {
            val postClient = mockHttpClient(mapOf("comments/1035361/replies" to "comments/reply_posted.json"))

            val reply = TraktCommentsApi(postClient).postCommentReplies(
                id = 1035361,
                comment = TraktPostComment(comment = "yes totally agree with this", spoiler = false),
            )

            assertThat(reply.id).isEqualTo(1047850)
            assertThat(reply.parentId).isEqualTo(1035361)
            assertThat(reply.comment).isEqualTo("yes totally agree with this")
        }
}
