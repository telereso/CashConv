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

package io.telereso.cashconv.client.remote

import io.telereso.kmp.core.Environment
import io.telereso.kmp.core.Http
import io.telereso.kmp.core.getPlatform
import io.telereso.kmp.core.httpClient
import io.telereso.cashconv.client.cache.SettingsManager
import io.telereso.cashconv.client.BuildKonfig
import io.ktor.client.plugins.*
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse
import io.ktor.http.path


internal class CashconvApiService(
    private val settingsManager: SettingsManager,
    private val httpClient: HttpClient,
    private val environment: Environment?,
    private val clientProtocol: String?,
    private val clientHost: String?
) {

    companion object {
        internal const val API_PATH_CURRENCIES = "/api/currencies.json"
        internal const val API_PATH_RATES = "/api/latest.json"
        private const val APP_ID = "b9be6939d6db4fa593fd2bcefefc12b0"

        private fun host(host: String?, environment: Environment?): String {
            return if (!host.isNullOrEmpty()) host else CashconvApiService.getHost(environment)
        }

        fun getHttpClient(
            shouldLogHttpRequests: Boolean,
            interceptors: List<Any?>? = listOf(),
            requestTimeout: Long?,
            connectionTimeout: Long?,
            environment: Environment?,
            clientProtocol: String?,
            clientHost: String?,
            appName: String?,
            appVersion: String?
        ): HttpClient {
            return httpClient(
                shouldLogHttpRequests,
                interceptors,
                Http.getUserAgent(
                    getPlatform(),
                    BuildKonfig.SDK_NAME,
                    BuildKonfig.SDK_VERSION,
                    appName,
                    appVersion,
                )
            ) {

                install(HttpTimeout) {
                    requestTimeoutMillis =
                        if (requestTimeout != null && requestTimeout > 0L) requestTimeout else Http.REQUEST_TIME_OUT_MILLIS
                    connectTimeoutMillis =
                        if (connectionTimeout != null && connectionTimeout > 0L) connectionTimeout else Http.CONNECTION_TIME_OUT_MILLIS
                }

                expectSuccess = true

                defaultRequest {
                    url {
                        protocol = Http.protocol(clientProtocol)

                        host = host(clientHost, environment)
                    }
                }
            }
        }

        fun getHost(environment: Environment?): String {
            return when (environment) {
                Environment.STAGING, Environment.PRODUCTION -> "openexchangerates.org"
                else -> {
                    "openexchangerates.org"
                }
            }
        }
    }

    suspend fun fetchCurrencies(): HttpResponse {
        return httpClient
            .get {
                url {
                    protocol = Http.protocol(clientProtocol)
                    host = host(clientHost, environment)
                    path(API_PATH_CURRENCIES)
                    parameter("app_id", APP_ID)
                }
            }
    }

    suspend fun fetchRates(): HttpResponse {
        return httpClient
            .get {
                url {
                    protocol = Http.protocol(clientProtocol)
                    host = host(clientHost, environment)
                    path(API_PATH_RATES)
                    parameter("app_id", APP_ID)
                }
            }
    }
}