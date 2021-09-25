package app.moviebase.trakt

import java.io.FileInputStream
import java.nio.file.Paths
import java.util.*

val properties by lazy {
    Properties().apply {
        val parent = Paths.get(System.getProperty("user.dir")).parent
        FileInputStream("$parent/local.properties").use {
            load(it)
        }
    }
}

fun buildTrakt(): Trakt {
    val clientId = properties.getProperty("TRAKT_CLIENT_ID")
    print("Trakt client ID: $clientId")
    return Trakt(clientId)
}
