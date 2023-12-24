package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import org.junit.Rule
import org.junit.Test
import components.ConverterCard
import components.CurrencyRow

class CurrencyRowTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyRow() {
        composeTestRule.setContent {
            CurrencyRow(
                title = "Ammount",
                currencies = listOf(Currency.jpy(), Currency.usd()),
                currency = Currency.jpy(),
                amount = "1",
            )
        }

        compareScreenshot(composeTestRule)
    }
}