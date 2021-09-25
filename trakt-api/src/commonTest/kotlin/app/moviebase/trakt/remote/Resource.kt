package app.moviebase.trakt.remote

expect class Resource(name: String) {
    fun readText(): String
}
