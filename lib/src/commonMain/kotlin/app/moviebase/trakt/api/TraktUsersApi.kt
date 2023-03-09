package app.moviebase.trakt.api

import app.moviebase.trakt.TraktExtended
import app.moviebase.trakt.core.getByPaths
import app.moviebase.trakt.core.parameterEndAt
import app.moviebase.trakt.core.parameterExtended
import app.moviebase.trakt.core.parameterStartAt
import app.moviebase.trakt.model.TraktHistoryItem
import app.moviebase.trakt.model.TraktList
import app.moviebase.trakt.model.TraktListType
import app.moviebase.trakt.model.TraktUserSettings
import app.moviebase.trakt.model.TraktUserSlug
import io.ktor.client.HttpClient
import kotlinx.datetime.Instant

class TraktUsersApi(private val client: HttpClient) {

    suspend fun getSettings(): TraktUserSettings = client.getByPaths(*pathUsers())

    suspend fun getLists(): List<TraktList> = client.getByPaths(*pathUsersName("lists"))

    /**
     * Example: users/id/history/type/item_id?start_at=2016-06-01T00%3A00%3A00.000Z&end_at=2016-07-01T23%3A59%3A59.000Z
     */
    suspend fun getHistory(
        userSlug: TraktUserSlug = TraktUserSlug.ME,
        listType: TraktListType? = null,
        itemId: Int? = null,
        extended: TraktExtended? = null,
        startAt: Instant? = null,
        endAt: Instant? = null,
    ): List<TraktHistoryItem> = client.getByPaths(*pathHistory(userSlug.name, listType, itemId)) {
        extended?.let { parameterExtended(extended) }
        startAt?.let { parameterStartAt(it) }
        endAt?.let { parameterEndAt(it) }
    }

    private fun pathUsers(vararg paths: String) = arrayOf("users", *paths)

    /**
     * Path: users/userSlug
     */
    private fun pathUsersName(userSlug: String, vararg paths: String) = arrayOf("users", userSlug, *paths)

    /**
     * Path: /users/userSlug/history/type/item_id
     */
    private fun pathHistory(
        userSlug: String,
        listType: TraktListType?,
        itemId: Int?,
        vararg paths: String,
    ) = listOfNotNull("users", userSlug, "history", listType?.value, itemId?.toString(), *paths).toTypedArray()
}
