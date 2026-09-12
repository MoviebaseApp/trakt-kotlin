package app.moviebase.trakt.model

import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TraktMovie(
    @SerialName("title") val title: String? = null,
    @SerialName("year") val year: Int? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("runtime") val runtime: Int? = null,
    @SerialName("ids") val ids: TraktItemIds? = null,
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("released") val released: String? = null,
    @SerialName("certification") val certification: String? = null,
    @SerialName("country") val country: String? = null,
    @SerialName("trailer") val trailer: String? = null,
    @SerialName("homepage") val homepage: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("rating") val rating: Float? = null,
    @SerialName("votes") val votes: Int? = null,
    @SerialName("comment_count") val commentCount: Int? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("genres") val genres: List<String> = emptyList(),
)

@Serializable
data class TraktMovieCounts(
    @SerialName("watcher_count") val watcherCount: Int? = null,
    @SerialName("play_count") val playCount: Int? = null,
    @SerialName("collected_count") val collectedCount: Int? = null,
    @SerialName("movie") val movie: TraktMovie? = null,
)

@Serializable
data class TraktTrendingMovie(
    @SerialName("watchers") val watchers: Int,
    @SerialName("movie") val movie: TraktMovie,
)

@Serializable
data class TraktAnticipatedMovie(
    @SerialName("list_count") val listCount: Int? = null,
    @SerialName("movie") val movie: TraktMovie,
)

@Serializable
data class TraktBoxOfficeMovie(
    @SerialName("revenue") val revenue: Long,
    @SerialName("movie") val movie: TraktMovie,
)

@Serializable
data class TraktMovieUpdate(
    @SerialName("updated_at") val updatedAt: Instant,
    @SerialName("movie") val movie: TraktMovie,
)

@Serializable
data class TraktAlias(
    @SerialName("title") val title: String,
    @SerialName("country") val country: String,
)

@Serializable
data class TraktRelease(
    @SerialName("country") val country: String,
    @SerialName("certification") val certification: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("release_type") val releaseType: String? = null,
    @SerialName("note") val note: String? = null,
)

@Serializable
data class TraktTranslation(
    @SerialName("title") val title: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("tagline") val tagline: String? = null,
    @SerialName("language") val language: String,
    @SerialName("country") val country: String? = null,
)

@Serializable
data class TraktCredits(
    @SerialName("cast") val cast: List<TraktCastMember> = emptyList(),
    @SerialName("crew") val crew: TraktCrewMembers? = null,
)

@Serializable
data class TraktCastMember(
    @SerialName("character") val character: String? = null,
    @SerialName("characters") val characters: List<String> = emptyList(),
    @SerialName("person") val person: TraktPerson,
)

@Serializable
data class TraktCrewMembers(
    @SerialName("production") val production: List<TraktCrewMember> = emptyList(),
    @SerialName("art") val art: List<TraktCrewMember> = emptyList(),
    @SerialName("crew") val crew: List<TraktCrewMember> = emptyList(),
    @SerialName("costume & make-up") val costumeMakeUp: List<TraktCrewMember> = emptyList(),
    @SerialName("directing") val directing: List<TraktCrewMember> = emptyList(),
    @SerialName("writing") val writing: List<TraktCrewMember> = emptyList(),
    @SerialName("sound") val sound: List<TraktCrewMember> = emptyList(),
    @SerialName("camera") val camera: List<TraktCrewMember> = emptyList(),
    @SerialName("visual effects") val visualEffects: List<TraktCrewMember> = emptyList(),
    @SerialName("lighting") val lighting: List<TraktCrewMember> = emptyList(),
    @SerialName("editing") val editing: List<TraktCrewMember> = emptyList(),
)

@Serializable
data class TraktCrewMember(
    @SerialName("job") val job: String? = null,
    @SerialName("jobs") val jobs: List<String> = emptyList(),
    @SerialName("person") val person: TraktPerson,
)

@Serializable
data class TraktStudio(
    @SerialName("name") val name: String,
    @SerialName("country") val country: String? = null,
    @SerialName("ids") val ids: TraktStudioIds? = null,
)

@Serializable
data class TraktStudioIds(
    @SerialName("trakt") val trakt: Int? = null,
    @SerialName("slug") val slug: String? = null,
    @SerialName("tmdb") val tmdb: Int? = null,
)
