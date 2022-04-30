package app.moviebase.trakt.remote

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import java.io.File

fun mockHttpClient(
    responses: Map<String, String>
) = HttpClient(MockEngine) {
    val jsonFiles = mutableMapOf<String, String>()
    responses.entries.forEach {
        jsonFiles["https://api.trakt.tv/${it.key}"] = it.value
    }
    val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))

    install(ContentNegotiation) {
        json(buildJson())
    }

    engine {
        addHandler { request ->
            val url = request.url.toString().decodeURLPart()

            val fileName = jsonFiles[url] ?: error("Unhandled url $url")
            val file = File("./src/jvmTest/resources/trakt/$fileName")
            val content = file.readText()
            respond(content = content, headers = headers)
        }
    }
}
