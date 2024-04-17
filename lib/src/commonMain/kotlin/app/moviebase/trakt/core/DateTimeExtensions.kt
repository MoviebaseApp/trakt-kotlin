package app.moviebase.trakt.core

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalDateTime

internal fun String.tryLocalDate(): LocalDate? = try {
    if (isBlank()) null else toLocalDate()
} catch (t: Throwable) {
    null
}

internal fun String.tryLocalDateTime(): LocalDateTime? = try {
    if (isBlank()) null else toInstant().toLocalDateTime(TimeZone.UTC)
} catch (t: Throwable) {
    null
}
