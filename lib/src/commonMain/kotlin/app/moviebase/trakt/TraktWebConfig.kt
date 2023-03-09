package app.moviebase.trakt

internal object TraktWebConfig {
    const val HOST = "api.trakt.tv"
    const val BASE_URL = "https://api.trakt.tv"
    const val WEBSITE_BASE_URL = "https://trakt.tv"

    const val VERSION = "2"
    const val OAUTH2_AUTHORIZATION_URL = "https://trakt.tv/oauth/authorize"

    const val PAGE_LIMIT = 10
    const val PAGE_LIMIT_RECOMMENDATION = 20
    const val PAGE_INITIAL = 0
    const val PAGE_MAX_LIMIT = 20
}

internal object TraktHeader {
    const val API_KEY = "trakt-api-key"
    const val API_VERSION = "trakt-api-version"
    const val PAGINATION_PAGE = "X-Pagination-Page"
    const val PAGINATION_PAGE_COUNT = "X-Pagination-Page-Count"
    const val PAGINATION_ITEM_COUNT = "X-Pagination-Item-Count"
}

internal object TraktUrlParameter {
    const val HOST = "trakt.tv"
    const val BASE_URL = "https://trakt.tv"

    const val PARAM_SEARCH = "search"
    const val COMMENTS = "comments"
    const val USERS = "users"
    const val PARAM_TRAKT_IMDB = "imdb"
    const val SHOWS = "shows"
    const val MOVIES = "movies"
    const val SEASONS = "seasons"
    const val EPISODES = "episodes"
}

enum class TraktExtended(val value: String) {
    FULL("full"),
    NOSEASONS("noseasons"),
    EPISODES("episodes"),
}
