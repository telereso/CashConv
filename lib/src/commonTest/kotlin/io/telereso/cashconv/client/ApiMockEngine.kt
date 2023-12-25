/*
 * MIT License
 *
 * Copyright (c) 2023 Telereso
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import io.telereso.cashconv.client.Resource
import io.telereso.cashconv.client.remote.CashconvApiService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import io.ktor.http.*

data class ApiMockEngineParams(
    val content: String,
    val status: HttpStatusCode = HttpStatusCode.OK,
    val headers: Headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
    val encodedPath:String? = null
) {
    companion object{
        val headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    }
}


/**
 * we can make this class as builder so we can pass params and different configuration per test run e. HTTP response and Path values
 * or we can use a Data approach like below.
 */
class ApiMockEngine {
    /**
     * The get() method is just a helper to easily access the engine property of an ApiMockEngine instance
     */
    fun get() = client.engine

    /**
     * responseHeaders defines a standard Content-Type: application/json header to be returned by the mock engine.
     */
    private val client = HttpClient(MockEngine) {
        engine {
            /**
             * Basically, when ApiMockEngine is used to make HTTP calls it will return the specific {Model}MockResponse object (as a JSON string)
             * when the URL path corresponds to the associated {resource}.
             * An HTTP OK (200) code and a standard Content-Type: application/json header will also be part of the response payload.
             */
            addHandler { request ->
                when (request.url.encodedPath) {
                    "/test" ->{
                        respond(
                            Resource("currencies.json").readText(),
                            HttpStatusCode.OK,
                            ApiMockEngineParams.headers
                        )
                    }
                    CashconvApiService.API_PATH_CURRENCIES -> {
                        respond(
                            Resource("currencies.json").readText(),
                            HttpStatusCode.OK,
                            ApiMockEngineParams.headers
                        )
                    }

                    CashconvApiService.API_PATH_RATES -> {
                        respond(
                            Resource("latest.json").readText(),
                            HttpStatusCode.OK,
                            ApiMockEngineParams.headers
                        )
                    }

                    else -> {
                        error("Unhandled ${request.url.encodedPath}")
                    }
                }
            }
        }
    }
}