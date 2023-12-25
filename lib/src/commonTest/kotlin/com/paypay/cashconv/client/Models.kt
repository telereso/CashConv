//package io.telereso.cashconv.client
//
//import io.telereso.cashconv.models.Amount
//import io.telereso.cashconv.models.fromJson
//import io.kotest.matchers.equals.shouldBeEqual
//import kotlin.test.Test
//
//class Models {
//
//    @Test
//    fun testAmount() {
//        Amount.fromJson(Resource("amount.json").readText())
//            .shouldBeEqual(Amount.default())
//    }
//}