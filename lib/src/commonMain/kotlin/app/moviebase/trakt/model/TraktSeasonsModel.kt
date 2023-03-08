// ktlint-disable filename

package app.moviebase.trakt.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class TraktSeasonEpisodes(
    val items: List<TraktEpisode>,
) {

    // use custom serializer because List cannot be used in client.get (JS inline method not allowed)
    @ExperimentalSerializationApi
    @Serializer(TraktSeasonEpisodes::class)
    companion object : KSerializer<TraktSeasonEpisodes> {

        override val descriptor = serializer().descriptor

        override fun serialize(encoder: Encoder, value: TraktSeasonEpisodes) {
            ListSerializer(TraktEpisode.serializer()).serialize(encoder, value.items)
        }

        override fun deserialize(decoder: Decoder): TraktSeasonEpisodes {
            val items = ListSerializer(TraktEpisode.serializer()).deserialize(decoder)
            return TraktSeasonEpisodes(items)
        }

        fun build(items: List<TraktEpisode>) = TraktSeasonEpisodes(items)
    }
}
