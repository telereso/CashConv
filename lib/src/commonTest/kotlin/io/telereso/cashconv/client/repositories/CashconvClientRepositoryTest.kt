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

package io.telereso.cashconv.client.repositories

import ApiMockEngine
import app.cash.turbine.test
import io.telereso.cashconv.client.Resource
import io.telereso.kmp.core.Http
import io.telereso.kmp.core.Settings
import io.telereso.cashconv.client.cache.SettingsManager
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.telereso.cashconv.client.remote.CashconvApiService
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class CashconvClientRepositoryTest {
    private lateinit var repository: CashconvClientRepository
    private lateinit var apiService: CashconvApiService

    companion object {
        val json = Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        }
        val testAmounts = listOf(
            "1",
            "2.0",
            "123",
            "1000",
            "2030",
            "23403",
            "1000000"
        )
    }

    @BeforeTest
    fun before() {

        val httpMockClient = HttpClient(
            ApiMockEngine().get()
        ) {
            install(ContentNegotiation) {
                json(Http.ktorConfigJson)
            }
        }

        val settingsManager = SettingsManager(Settings.getInMemory())
        apiService = CashconvApiService(settingsManager, httpMockClient, null, null, null)
        repository = CashconvClientRepository(settingsManager, apiService, null)
    }

    @AfterTest
    fun after() {
    }

    @Test
    fun testInit() = runTest {
        repository.init()
        repository.currenciesRatesFlow.test {
            awaitItem().size.shouldBe(169)
        }
    }

    @Test
    fun testFetchCurrencies() = runTest {
        val res = repository.fetchCurrencies()
        res.size.shouldBe(170)
    }

    @Test
    fun testGetCurrencies() = runTest {
        val res = repository.fetchCurrenciesIfNeeded()
        res.size.shouldBe(170)
    }

    @Test
    fun testBaseCurrencyFlow() = runTest {
        repository.fetchCurrencies()
        repository.baseCurrencyFlow.test {
            awaitItem().code.shouldBe("USD")
        }
    }


    @Test
    fun testFetchRates() = runTest {
        val res = repository.fetchRates()
        res.size.shouldBe(169)
    }

    @Test
    fun testGetRates() = runTest {
        val res = repository.getRates()
        res.size.shouldBe(169)
    }

    @Test
    fun testCurrenciesRatesFlow() = runTest {
        repository.fetchRatesIfNeeded()
        repository.currenciesRatesFlow.test {
            awaitItem().size.shouldBe(169)
        }
    }

    @Test
    fun testConvert() = runTest {
        repository.fetchRates()
        repository.getRates().apply {
            forEach { from ->
                val resultMap =
                    json.decodeFromString<Map<String, Map<String, String>>>(Resource("conversions/${from.code}.json").readText())
                forEach { to ->
                    testAmounts.forEach { amount ->
                        repository
                            .convert(amount, from.code, to.code)
                            .shouldBe(resultMap[to.code]!![amount])
                    }
                }
            }
        }
    }

    @Test
    fun testToDoubleString() {

        "asdf".toDoubleString(1)
            .shouldBe("0.0")

        "1.0".toDoubleString(1)
            .shouldBe("1.0")

        "1.0".toDoubleString(2)
            .shouldBe("1.00")

        "1.0".toDoubleString(3)
            .shouldBe("1.000")

        "2.0".toDoubleString(2)
            .shouldBe("2.00")

        "2234.0".toDoubleString(2)
            .shouldBe("2234.00")


        1.0.toDoubleString(1)
            .shouldBe("1.0")

        1.0.toDoubleString(2)
            .shouldBe("1.00")

        1.0.toDoubleString(3)
            .shouldBe("1.000")

        2.0.toDoubleString(2)
            .shouldBe("2.00")

        2234.0.toDoubleString(2)
            .shouldBe("2234.00")

        2234.0000.toDoubleString(2)
            .shouldBe("2234.00")

    }


}