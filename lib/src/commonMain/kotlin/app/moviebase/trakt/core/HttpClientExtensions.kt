package app.moviebase.trakt.core

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.util.pipeline.PipelinePhase

/**
 * Build easier to read paths with endPoint(...)
 */
internal suspend inline fun <reified T> HttpClient.getByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {},
): T = get(urlString = buildPaths(*paths), block = block).body()

/**
 * Build easier to read paths with endPoint(...)
 */
internal suspend inline fun <reified T> HttpClient.postByPaths(
    vararg paths: String,
    block: HttpRequestBuilder.() -> Unit = {},
): T = post(urlString = buildPaths(*paths), block = block).body()

fun buildPaths(vararg paths: String): String = paths.joinToString(separator = "/")
fun buildPaths(paths: Collection<String>): String = paths.joinToString(separator = "/")

typealias RequestInterceptor = suspend (HttpRequestBuilder) -> Unit

fun HttpClient.interceptRequest(phase: PipelinePhase = HttpRequestPipeline.Render, interceptor: RequestInterceptor) =
    requestPipeline.intercept(phase) { interceptor(context) }
