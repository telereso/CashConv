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
