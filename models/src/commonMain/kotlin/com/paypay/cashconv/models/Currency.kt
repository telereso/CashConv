package io.telereso.cashconv.models

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import kotlin.jvm.JvmStatic

@Serializable
// @JsExport
data class Currency(
    val code: String,
    val rate: Double,
    val name: String = "",
    val converted: String = "-"
) {

    fun emptyRate(): Boolean {
        return rate == 0.0
    }

    fun isBase(): Boolean {
        return code == BASE.code
    }

    companion object {
        var BASE = base("USD")

        fun base(code: String): Currency {
            return Currency(code, 1.0)
        }

        fun usd(): Currency {
            return Currency("USD", 0.0, "United States Dollar")
        }

        fun jpy(): Currency {
            return Currency("JPY", 0.0, "Japanese Yen")
        }

        @JvmStatic
        fun from(code: String): Currency {
            return Currency(code, 0.0)
        }
    }
}
