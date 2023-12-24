package io.telereso.cashconv.client.models

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

@Serializable
data class RatesResponse(val rates: Map<String, Double>, val base: String)