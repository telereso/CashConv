//package io.telereso.cashconv.client
//
//import ApiMockEngine
//import app.cash.turbine.test
//import io.telereso.kmp.core.Http
//import io.telereso.kmp.core.Settings
//import io.telereso.cashconv.client.cache.SettingsManager
//import io.kotest.matchers.shouldBe
//import io.ktor.client.HttpClient
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.serialization.kotlinx.json.json
//import io.telereso.cashconv.client.remote.CashconvApiService
//import io.telereso.cashconv.client.repositories.CashconvClientRepository
//import io.telereso.cashconv.client.repositories.CashconvClientRepositoryTest
//import io.telereso.cashconv.models.Amount
//import io.telereso.cashconv.models.Currency
//import io.kotest.matchers.booleans.shouldBeFalse
//import io.kotest.matchers.booleans.shouldBeTrue
//import io.kotest.matchers.nulls.shouldBeNull
//import io.kotest.matchers.nulls.shouldNotBeNull
//import io.telereso.kmp.core.await
//import kotlinx.coroutines.test.runTest
//import kotlin.test.AfterTest
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//
//class CashconvClientMangerTest {
//    private lateinit var manger: CashconvClientManager
//
//
//    private fun mockRepo(): Pair<CashconvClientRepository, SettingsManager> {
//        val httpMockClient = HttpClient(
//            ApiMockEngine().get()
//        ) {
//            install(ContentNegotiation) {
//                json(Http.ktorConfigJson)
//            }
//        }
//        val settingsManager = SettingsManager(Settings.getInMemory())
//        val apiService = CashconvApiService(settingsManager, httpMockClient, null, null, null)
//        return CashconvClientRepository(settingsManager, apiService, null) to settingsManager
//    }
//
//    @BeforeTest
//    fun before() {
//
//        val (repo, settingManger) = mockRepo()
//        manger = CashconvClientManager
//            .Builder()
//            .withRepository(repo)
//            .withSettingsManger(settingManger)
//            .enableWarmUpOnStartUp(false)
//            .build()
//    }
//
//    @AfterTest
//    fun after() {
//        CashconvClientManager.getInstanceOrNull()?.apply {
//            destroy("after")
//        }
//    }
//
//    @Test
//    fun testClientBuilder() {
//        CashconvClientManager.client {
//            enableWarmUpOnStartUp(false)
//            withSettingsManger(SettingsManager(Settings.getInMemory()))
//        }
//        CashconvClientManager.getInstanceOrNull().apply {
//            shouldNotBeNull()
//            destroy("test")
//        }
//    }
//
//    @Test
//    fun testDestroy() {
//        CashconvClientManager.client {
//            enableWarmUpOnStartUp(true)
//        }
//        CashconvClientManager.getInstance().destroy("test")
//        CashconvClientManager.getInstanceOrNull().shouldBeNull()
//    }
//
//    @Test
//    fun testClearData() = runTest {
//        manger.fetchRates().await()
//        manger.shouldFetchRates().shouldBeFalse()
//        manger.clearData()
//        manger.shouldFetchRates().shouldBeTrue()
//    }
//
//    @Test
//    fun testFetchCurrencies() = runTest {
//        val res = manger.fetchCurrencies().await()
//        res.size.shouldBe(170)
//    }
//
//    @Test
//    fun testFetchCurrenciesIfNeeded() = runTest {
//        val res = manger.fetchCurrenciesIfNeeded().await()
//        res.size.shouldBe(170)
//    }
//
//    @Test
//    fun testGetCurrencies() = runTest {
//        val res = manger.getCurrencies().await()
//        res.size.shouldBe(170)
//    }
//
//    @Test
//    fun testShouldFetchRates() = runTest {
//        manger.shouldFetchRates().shouldBeTrue()
//        manger.fetchRatesIfNeeded().await()
//        manger.shouldFetchRates().shouldBeFalse()
//    }
//
//    @Test
//    fun testBaseCurrencyFlow() = runTest {
//        manger.fetchCurrencies()
//        manger.getBaseCurrencyFlow().test {
//            awaitItem().code.shouldBe("USD")
//        }
//    }
//
//
//    @Test
//    fun testFetchRates() = runTest {
//        val res = manger.fetchRates().await()
//        res.size.shouldBe(169)
//    }
//
//    @Test
//    fun testGetRates() = runTest {
//        val res = manger.getRates().await()
//        res.size.shouldBe(169)
//    }
//
//    @Test
//    fun testCurrenciesRatesFlow() = runTest {
//        manger.fetchRatesIfNeeded().await()
//        manger.getCurrenciesRatesFlow().test {
//            awaitItem().size.shouldBe(169)
//        }
//    }
//
//    @Test
//    fun testFloaingDigest() = runTest {
//        CashconvClientManager.client {
//            withRepository(mockRepo().first)
//            withFloatingDigit(3)
//        }
//        CashconvClientManager
//            .getInstance().apply {
//                fetchRates().await()
//                convert("1", Currency.usd(), Currency.jpy())
//                    .shouldBe("149.535")
//            }
//    }
//
//    @Test
//    fun testConvert() = runTest {
//        manger.fetchRates().await()
//        manger.getRates().await().apply {
//            forEach { from ->
//                val resultMap =
//                    CashconvClientRepositoryTest.json.decodeFromString<Map<String, Map<String, String>>>(
//                        Resource("conversions/${from.code}.json").readText()
//                    )
//                forEach { to ->
//                    CashconvClientRepositoryTest.testAmounts.forEach { amount ->
//                        val res = resultMap[to.code]!![amount]
//                        manger
//                            .convert(amount, from, to)
//                            .shouldBe(res)
//
//                        manger
//                            .convert(Amount(from, amount, to))
//                            .convertedValue
//                            .shouldBe(res)
//                    }
//                }
//            }
//        }
//    }
//}