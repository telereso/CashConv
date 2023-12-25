//package io.telereso.cashconv.client.remote
//
//import ApiMockEngine
//import io.telereso.kmp.core.Settings
//import io.telereso.kmp.core.Http
//import io.telereso.cashconv.client.cache.SettingsManager
//import io.telereso.cashconv.client.models.RatesResponse
//import io.kotest.matchers.nulls.shouldNotBeNull
//import io.kotest.matchers.shouldBe
//import io.ktor.client.HttpClient
//import io.ktor.client.call.body
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.http.HttpStatusCode
//import io.ktor.serialization.kotlinx.json.json
//import io.telereso.kmp.core.Environment
//import kotlinx.coroutines.test.runTest
//import kotlin.test.AfterTest
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//
//class CashconvApiServiceTest {
//
//    private lateinit var apiService: CashconvApiService
//
//    /**
//     * https://kotest.io/docs/assertions/ktor-matchers.html
//     */
//    @BeforeTest
//    fun before() {
//        val httpClient = HttpClient(
//            ApiMockEngine().get()
//        ) {
//            install(ContentNegotiation) {
//                json(Http.ktorConfigJson)
//            }
//        }
//        apiService = CashconvApiService(
//            SettingsManager(Settings.getInMemory()),
//            httpClient,
//            null,
//            null,
//            null
//        )
//    }
//
//    @AfterTest
//    fun after() {
//
//    }
//
//    @Test
//    fun getHttpClient() = runTest {
//
//        val httpRequest = CashconvApiService
//            .getHttpClient(
//                shouldLogHttpRequests = true,
//                interceptors = listOf(),
//                requestTimeout = 3000,
//                connectionTimeout = 3000,
//                environment = null,
//                clientProtocol = null,
//                clientHost = null,
//                appName = "test",
//                appVersion = "test"
//            )
//
//        httpRequest.shouldNotBeNull()
//    }
//
//    @Test
//    fun getHost() {
//        CashconvApiService
//            .getHost(Environment.STAGING)
//            .shouldBe("openexchangerates.org")
//
//        CashconvApiService
//            .getHost(Environment.PRODUCTION)
//            .shouldBe("openexchangerates.org")
//
//        CashconvApiService
//            .getHost(null)
//            .shouldBe("openexchangerates.org")
//    }
//
//    @Test
//    fun testFetchCurrencies() = runTest {
//        val res = apiService.fetchCurrencies()
//        res.status.shouldBe(HttpStatusCode.OK)
//        val map = res.body<Map<String, String>>()
//        map.size.shouldBe(170)
//    }
//
//
//    @Test
//    fun testFetchRates() = runTest {
//        val httpRes = apiService.fetchRates()
//        httpRes.status.shouldBe(HttpStatusCode.OK)
//        val res = httpRes.body<RatesResponse>()
//        res.base.shouldBe("USD")
//        res.rates.size.shouldBe(169)
//
//    }
//}