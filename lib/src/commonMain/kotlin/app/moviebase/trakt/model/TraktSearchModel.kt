package app.moviebase.trakt.model

import app.moviebase.trakt.TraktExtended
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class TraktSearchResults(
    val items: List<TraktSearchResult>,
) {

    // use custom serializer because List cannot be used in client.get (JS inline method not allowed)
    @ExperimentalSerializationApi
    @Serializer(TraktSearchResults::class)
    companion object : KSerializer<TraktSearchResults> {

        override val descriptor = serializer().descriptor

        override fun serialize(encoder: Encoder, value: TraktSearchResults) {
            ListSerializer(TraktSearchResult.serializer()).serialize(encoder, value.items)
        }

        override fun deserialize(decoder: Decoder): TraktSearchResults {
            val items = ListSerializer(TraktSearchResult.serializer()).deserialize(decoder)
            return TraktSearchResults(items)
        }
    }
}

@Serializable
data class TraktSearchResult(
    val type: TraktMediaType,
    val movie: TraktMovie? = null,
    val show: TraktShow? = null,
    val episode: TraktEpisode? = null,
) {

    val ids: TraktItemIds?
        get() = when {
            movie != null -> movie.ids
            episode != null -> episode.ids // could have show and episode
            show != null -> show.ids
            else -> null
        }
}

enum class TraktIdType(val value: String) {
    IMDB("imdb"),
    TMDB("tmdb"),
    TVDB("tvdb"),
}

enum class TraktSearchType(val value: String) {
    MOVIE("movie"),
    SHOW("show"),
    EPISODE("episode"),
    PERSON("person"),
    LIST("list")
}

// TODO: field can be limited by media type
@Serializable
data class TraktSearchQuery(
    @SerialName("query") val query: String? = null,
    @SerialName("years") val years: String? = null,
    @SerialName("genres") val genres: String? = null,
    @SerialName("languages") val languages: String? = null,
    @SerialName("countries") val countries: String? = null,
    @SerialName("runtimes") val runtimes: String? = null,
    @SerialName("ratings") val ratings: String? = null,
    @SerialName("certifications") val certifications: String? = null,
    @SerialName("networks") val networks: String? = null,
    @SerialName("status") val status: String? = null,
    @SerialName("extended") val extended: TraktExtended? = null,
    @SerialName("page") val page: Int? = null,
    @SerialName("limit") val limit: Int? = null,
) {

    val parameters: Map<String, String?>
        get() = mapOf(
            "query" to query,
            "years" to years,
            "genres" to genres,
            "languages" to languages,
            "countries" to countries,
            "runtimes" to runtimes,
            "ratings" to ratings,
            "certifications" to certifications,
            "networks" to networks,
            "status" to status,
            "extended" to extended?.value,
            "page" to page?.toString(),
            "limit" to limit?.toString(),
        )
}
