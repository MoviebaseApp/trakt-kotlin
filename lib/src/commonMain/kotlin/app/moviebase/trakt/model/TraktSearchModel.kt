package app.moviebase.trakt.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable
data class TraktSearchResults(
    val items: List<TraktSearchResult>
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
    val episode: TraktEpisode? = null
) {

    val ids: TraktIds?
        get() = when {
            movie != null   -> movie.ids
            episode != null -> episode.ids // could have show and episode
            show != null    -> show.ids
            else            -> null
        }

}

enum class TraktSearchType(val value: String) {
    IMDB("imdb"),
    TMDB("tmdb"),
    TVDB("tvdb"),
}

