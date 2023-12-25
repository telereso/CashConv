package io.telereso.cashconv.models

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
// @JsExport
data class Amount(
    val currency: Currency,
    val value: String,
    val convertCurrency: Currency,
    val convertedValue: String = "-"
) {

    fun json(): String {
        return toJson()
    }

    fun jsonPretty(): String {
        return toJsonPretty()
    }

    companion object {
        fun default() = Amount(Currency.usd(), "", Currency.jpy())
    }
}
