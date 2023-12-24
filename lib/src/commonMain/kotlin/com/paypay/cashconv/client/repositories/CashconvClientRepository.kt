package io.telereso.cashconv.client.repositories

import io.telereso.kmp.core.Log.logDebug
import io.telereso.cashconv.client.cache.SettingsManager
import io.telereso.cashconv.client.models.RatesResponse
import io.telereso.cashconv.client.remote.CashconvApiService
import io.telereso.cashconv.models.Currency
import io.ktor.client.call.body
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow

internal class CashconvClientRepository(
    private val settingsManager: SettingsManager,
    private val apiService: CashconvApiService,
    private val floatingDigit: Int?,
//    private val dao: Dao
) {

    internal val currenciesRatesFlow = MutableStateFlow<List<Currency>>(listOf())
    internal val baseCurrencyFlow = MutableStateFlow(Currency.usd())
    internal var ratesMap = hashMapOf<String, Currency>()

    suspend fun init() {
//        dao.getAllRocketLaunches()
        // load currencies from cache
        val rates = settingsManager.rates
        currenciesRatesFlow.emit(settingsManager.rates)
        baseCurrencyFlow.emit(settingsManager.base)
        rates.forEach {
            ratesMap[it.code] = it
        }
        fetchRatesIfNeeded()

    }

    suspend fun getCurrencies(): List<Currency> {
        return if (settingsManager.shouldFetchCurrencies)
            runCatching { fetchCurrencies() }.getOrElse { settingsManager.currencies }
        else
            settingsManager.currencies
    }

    suspend fun fetchCurrenciesIfNeeded(): List<Currency> {
        logDebug("fetchCurrenciesIfNeeded")
        return getCurrencies()
    }

    suspend fun fetchCurrencies(): List<Currency> {
        logDebug("fetchCurrencies")
        val response = apiService.fetchCurrencies()
        if (response.status.isSuccess()) {
            val currencies = response
                .body<Map<String, String>>()
                .map { Currency(it.key, 0.0, name = it.value) }

            settingsManager.currencies = currencies
            return currencies

        } else {
            throw Throwable("Failed fetchCurrencies")
        }
    }

    suspend fun getRates(): List<Currency> {
        logDebug("getRates")
        return if (settingsManager.shouldFetchRates)
            runCatching { fetchRates() }.getOrElse { settingsManager.rates }
        else
            settingsManager.rates
    }

    suspend fun fetchRates(): List<Currency> {
        logDebug("fetchRates")
        val response = apiService.fetchRates()
        if (response.status.isSuccess()) {
            val res = response
                .body<RatesResponse>()

            val rates = res
                .rates
                .map { Currency(it.key, it.value) }

            val base = Currency.base(res.base)

            Currency.BASE = base
            settingsManager.base = base
            settingsManager.rates = rates
            rates.forEach {
                ratesMap[it.code] = it
            }
            currenciesRatesFlow.emit(rates)
            baseCurrencyFlow.emit(base)

            return rates

        } else {
            throw Throwable("Failed fetchRates")
        }
    }

    suspend fun fetchRatesIfNeeded(): List<Currency> {
        logDebug("fetchRatesIfNeeded")
        return getRates()
    }


    fun convert(
        amount: String?,
        from: String,
        to: String,
        applyFloatingLimit: Boolean = true
    ): String {
        val fromCurrency = ratesMap[from]
        val toCurrency = ratesMap[to]

        if (amount == null || fromCurrency == null || toCurrency == null
        ) return "-"

        return runCatching {
            val converted = when {
                fromCurrency.isBase() -> {
                    amount.toDouble().times(toCurrency.rate)
                }

                toCurrency.isBase() -> {
                    amount.toDouble().div(fromCurrency.rate)
                }

                else -> {
                    amount.toDouble().times(toCurrency.rate / fromCurrency.rate)
                }
            }
            if (applyFloatingLimit)
                converted.toDoubleString(floatingDigit)
            else
                converted.toString()
        }.getOrElse { "-" }
    }
}

fun String.toDoubleString(digit: Int?): String {
    return runCatching {
        val doubleString = toDouble().toString()

        if (digit == null || digit <= 0) return doubleString

        if (!contains("."))
            return "${this.toLong()}.${"0".repeat(digit)}"

        if (doubleString.contains("E")) return doubleString

        val parts = this.split(".")
        val floating = parts[1]
        var extraZeros = digit - floating.length
        if (extraZeros < 0 || extraZeros > digit)
            extraZeros = 0

        return "${parts[0].toLong()}.${parts[1].take(digit)}${"0".repeat(extraZeros)}"
    }.getOrElse {
        "0.0"
    }
}

fun Double.toDoubleString(digit: Int?): String {
    return toString().toDoubleString(digit)
}
