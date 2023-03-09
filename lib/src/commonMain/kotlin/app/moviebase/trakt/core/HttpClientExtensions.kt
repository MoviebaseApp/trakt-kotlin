package app.moviebase.trakt.core

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.util.pipeline.PipelinePhase

typealias RequestInterceptor = suspend (HttpRequestBuilder) -> Unit
typealias ResponseInterceptor = suspend (HttpClientCall) -> Unit

fun HttpClient.interceptRequest(phase: PipelinePhase = HttpRequestPipeline.Render, interceptor: RequestInterceptor) =
    requestPipeline.intercept(phase) { interceptor(context) }

/**
 * Interceptor for throwing an exception must run before [HttpResponsePipeline.Transform] phase.
 */
fun HttpClient.interceptResponse(phase: PipelinePhase = HttpResponsePipeline.Parse, interceptor: ResponseInterceptor) =
    responsePipeline.intercept(phase) { interceptor(context) }
