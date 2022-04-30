package app.moviebase.trakt

object TraktWebConfig {
    const val BASE_URL = "https://api.trakt.tv"
    const val VERSION = "2"
    const val OAUTH2_AUTHORIZATION_URL = "https://trakt.tv/oauth/authorize"

    const val PAGE_LIMIT = 10
    const val PAGE_LIMIT_RECOMMENDATION = 20
}

object TraktHeader {
    const val API_KEY = "trakt-api-key"
    const val API_VERSION = "trakt-api-version"
    const val PAGINATION_PAGE = "X-Pagination-Page"
    const val PAGINATION_PAGE_COUNT = "X-Pagination-Page-Count"
    const val PAGINATION_ITEM_COUNT = "X-Pagination-Item-Count"
}

object TraktUrlParameter {
    const val HOST = "trakt.tv"
    const val BASE_URL = "https://trakt.tv"

    const val PARAM_SEARCH = "search"
    const val PARAM_COMMENTS = "comments"
    const val PARAM_TRAKT_IMDB = "imdb"
    const val SHOWS = "shows"
    const val MOVIES = "movies"
    const val SEASONS = "seasons"
    const val EPISODES = "episodes"
}

object TraktExtended {
    const val FULL = "full"
}
