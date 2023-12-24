package io.telereso.cashconv.client

import ApiMockEngine
import io.telereso.cashconv.client.cache.SettingsManager
import io.telereso.cashconv.client.remote.CashconvApiService
import io.telereso.cashconv.client.repositories.CashconvClientRepository
import io.telereso.cashconv.client.repositories.CashconvClientRepositoryTest
import io.telereso.cashconv.models.Currency
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.telereso.kmp.core.Http
import io.telereso.kmp.core.Settings
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import java.io.File

class Conversions {

    private lateinit var repository: CashconvClientRepository
    private lateinit var apiService: CashconvApiService

    @Before
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

    private fun getFile(name: String) = File("${TestUtils.RESOURCE_PATH}/$name")
    private fun getConversions(name: String): File {
        return getFile("conversions").apply {
            if (!exists())
                mkdir()
        }.resolve(name).apply {
            if (!exists())
                createNewFile()
        }
    }

    @Test
    @Ignore // uncomment if you want to generate new snapshot
    fun generateConversionTable() = runTest {
        repository.fetchRates()
        val map = hashMapOf<String, MutableMap<String, MutableMap<String, String>>>()

        fun convert(
            amount: String,
            from: Currency,
            to: Currency,
            applyFloatingLimit: Boolean = true
        ) {
            val converted = repository.convert(amount, from.code, to.code, applyFloatingLimit)
            val log = "$amount ${from.code} -> ${to.code} = $converted"
//            println(log)

            map.getOrPut(from.code) {
                hashMapOf()
            }.getOrPut(to.code) {
                hashMapOf()
            }[amount] = converted

        }
        repository.getRates().apply {
            forEach { from ->
                forEach { to ->
                    CashconvClientRepositoryTest.testAmounts.forEach {
                        convert(it, from, to)
                    }
                }
            }
        }

        map.forEach {
            getConversions("${it.key}.json")
                .writeText(CashconvClientRepositoryTest.json.encodeToString(it.value))
        }

    }
}