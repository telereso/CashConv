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

package io.telereso.cashconv.client

import io.telereso.cashconv.client.*
import io.telereso.kmp.core.*
import io.telereso.cashconv.client.cache.SettingsManager
import io.telereso.cashconv.client.remote.CashconvApiService
import kotlinx.coroutines.*
import io.telereso.cashconv.client.repositories.CashconvClientRepository
import io.telereso.cashconv.models.Amount
import io.telereso.cashconv.models.Currency
import io.ktor.client.plugins.*
import io.ktor.client.statement.*
import io.telereso.kmp.annotations.ReactNativeExport
import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport
import kotlin.js.JsName
import kotlin.jvm.JvmStatic

/**
 * This class acts as the point of entry into the client sdk.
 *
 * @constructor a private constructor due to the build pattern.
 */
@ExperimentalJsExport
@JsExport
@ReactNativeExport
class CashconvClientManager private constructor(
    private val builder: Builder,
    config: Config? = null
) {

    companion object {
        /**
         * a Kotlin DSL fun that uses scope to build the CashconvClientManager.
         */
        inline fun client(
            block: Builder.() -> Unit
        ) =
            Builder()
                .apply(block)
                .build()

        private var instance: CashconvClientManager? = null

        @Throws(NullPointerException::class)
        @JvmStatic
        fun getInstance(): CashconvClientManager {
            if (instance == null) throw NullPointerException("CashconvClientManager instance cannot be null!")
            return instance!!
        }

        @JvmStatic
        fun getInstanceOrNull(): CashconvClientManager? {
            return instance
        }

        @JvmStatic
        fun builder(): Builder {
            return Builder()
        }
    }

    private val settingsManager by lazy {
        builder.settingsManager
            ?: SettingsManager(if (builder.inMemorySettings) Settings.getInMemory() else settings)
    }

    private val repo: CashconvClientRepository by lazy {
        builder.repository ?: run {
            val httpClient = CashconvApiService.getHttpClient(
                config?.builder?.logHttpRequests ?: false,
                config?.builder?.interceptors,
                config?.builder?.requestTimeoutMillis,
                config?.builder?.connectTimeoutMillis,
                config?.builder?.environment,
                config?.builder?.protocol,
                config?.builder?.host,
                config?.builder?.appName,
                config?.builder?.appVersion,
            )
            CashconvClientRepository(
                settingsManager,
                CashconvApiService(
                    settingsManager,
                    httpClient,
                    config?.builder?.environment,
                    config?.builder?.protocol,
                    config?.builder?.host
                ),
                builder.floatingDigit
            )
        }
    }

    init {
        instance = this
        if (builder.warmUpOnStartUp)
            load()
    }

    private fun load() {
        Task.execute {
            repo.init()
        }
    }

    fun destroy(reason: String) {
        instance = null
    }

    fun clearData() {
        settingsManager.clearAllSettings()
    }

    fun getCurrenciesRatesFlow(): CommonFlow<List<Currency>> {
        return repo.currenciesRatesFlow.asCommonFlow()
    }

    /**
     * Fetches RocketLaunch from network and cache based on the configuration sent.
     * no cache available.
     * @return a Task array of RocketLaunch items.
     */
    fun getCurrencies(): Task<List<Currency>> {
        return Task.execute {
            repo.getCurrencies()
        }
    }

    fun fetchCurrencies(): Task<List<Currency>> {
        return Task.execute {
            repo.fetchCurrencies()
        }
    }

    fun fetchCurrenciesIfNeeded(): Task<List<Currency>> {
        return Task.execute {
            repo.fetchCurrenciesIfNeeded()
        }
    }

    fun getRates(): Task<Array<Currency>> {
        return Task.execute {
            repo.getRates().toTypedArray()
        }
    }

    fun fetchRates(): Task<Array<Currency>> {
        return Task.execute {
            repo.fetchRates().toTypedArray()
        }
    }

    fun shouldFetchRates(): Boolean {
        return settingsManager.shouldFetchRates
    }

    fun fetchRatesIfNeeded(): Task<List<Currency>> {
        return Task.execute {
            repo.fetchRatesIfNeeded()
        }
    }

    fun getBaseCurrencyFlow(): CommonFlow<Currency> {
        return repo.baseCurrencyFlow.asCommonFlow()
    }

    fun convert(amount: String?, from: Currency, to: Currency): String {
        return repo.convert(amount, from.code, to.code)
    }

    @JsName("convertAmount")
    fun convert(amount: Amount): Amount {
        return amount.copy(
            convertedValue = convert(amount.value, amount.currency, amount.convertCurrency)
        )
    }

    /**
     * We may need to pass some compulsory values to the builder.
     * Mandatory param values are part of the constructor.
     */
    class Builder {

        internal var warmUpOnStartUp: Boolean = true
        internal var repository: CashconvClientRepository? = null
        internal var settingsManager: SettingsManager? = null
        internal var config: Config? = null
        internal var floatingDigit: Int? = null
        internal var inMemorySettings: Boolean = false

        /**
         * set a listener for overall client events
         */
        fun withConfig(config: Config): Builder {
            this.config = config
            return this
        }

        fun withFloatingDigit(digit: Int?): Builder {
            if (digit != null && digit <= 0) return this
            floatingDigit = digit
            return this
        }

        fun withInMemorySettings(enabled: Boolean = true): Builder {
            inMemorySettings = enabled
            return this
        }

        internal fun withRepository(repository: CashconvClientRepository): Builder {
            this.repository = repository
            return this
        }

        internal fun withSettingsManger(settingsManager: SettingsManager): Builder {
            this.settingsManager = settingsManager
            return this
        }

        fun enableWarmUpOnStartUp(enable: Boolean): Builder {
            this.warmUpOnStartUp = enable
            return this
        }

        fun build(): CashconvClientManager {
            return instance ?: CashconvClientManager(this, config)
        }
    }
}

fun Amount.convert(): Amount {
    return CashconvClientManager.getInstanceOrNull()?.convert(this) ?: this
}
