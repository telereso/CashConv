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

package io.telereso.cashconv.client.cache

import io.telereso.cashconv.models.Currency
import io.telereso.cashconv.models.fromJson
import io.telereso.cashconv.models.fromJsonArray
import io.telereso.cashconv.models.toJson
import io.telereso.kmp.core.Settings
import io.telereso.kmp.core.get
import io.telereso.kmp.core.set
import kotlin.time.DurationUnit
import kotlin.time.toDuration

internal class SettingsManager(private val settings: Settings) {

    companion object {
        const val KEY_BASE = "settings_base"
        const val KEY_CURRENCIES = "settings_currencies"
        const val KEY_SHOULD_FETCH_CURRENCIES = "settings_should_fetch_currencies"
        const val KEY_SHOULD_RATES = "settings_should_rates"
        const val KEY_SHOULD_FETCH_RATES = "settings_should_fetch_rates"

    }

    var base: Currency
        get() = runCatching {
            Currency.fromJson(
                settings[KEY_BASE] ?: "{}"
            )
        }.getOrElse { Currency.usd() }
        set(value) {
            settings[KEY_BASE] = value.toJson()
        }

    var shouldFetchCurrencies: Boolean
        get() = settings.getExpirableString(KEY_SHOULD_FETCH_CURRENCIES)?.toBoolean() ?: true
        set(value) {
            settings.putExpirableString(
                KEY_SHOULD_FETCH_CURRENCIES,
                value.toString(),
                30.toDuration(DurationUnit.MINUTES)
            )
        }

    var currencies: List<Currency>
        get() = Currency.fromJsonArray(settings[KEY_CURRENCIES] ?: "[]").toList()
        set(value) {
            settings[KEY_CURRENCIES] = Currency.toJson(value.toTypedArray())
            shouldFetchCurrencies = false
        }

    var shouldFetchRates: Boolean
        get() = settings.getExpirableString(KEY_SHOULD_FETCH_RATES)?.toBoolean() ?: true
        set(value) {
            settings.putExpirableString(
                KEY_SHOULD_FETCH_RATES,
                value.toString(),
                30.toDuration(DurationUnit.MINUTES)
            )
        }

    var rates: List<Currency>
        get() = Currency.fromJsonArray(settings[KEY_SHOULD_RATES] ?: "[]").toList()
        set(value) {
            settings[KEY_SHOULD_RATES] = Currency.toJson(value.toTypedArray())
            shouldFetchRates = false
        }


    fun clearAllSettings() {
        settings.clear()
    }

}
