package app.moviebase.trakt.url

import app.moviebase.trakt.TraktUrlParameter
import app.moviebase.trakt.TraktWebConfig

object TraktUrlBuilder {

    /**
     * Example: https://trakt.tv/users/andrewbloom
     */
    fun buildUserPage(userId: String) = "${TraktWebConfig.WEBSITE_BASE_URL}/${TraktUrlParameter.USERS}/$userId"

    /**
     * Example: https://trakt.tv/comments/283816
     */
    fun buildCommentPage(commentId: Int) = "${TraktWebConfig.WEBSITE_BASE_URL}/${TraktUrlParameter.COMMENTS}/$commentId"
}
