package app.moviebase.trakt.api

import app.moviebase.trakt.core.mockHttpClient
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
