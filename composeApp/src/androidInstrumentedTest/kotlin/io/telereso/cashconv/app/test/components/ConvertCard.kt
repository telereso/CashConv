package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import org.junit.Rule
import org.junit.Test
import components.ConverterCard

class ConvertCardTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testConverterCard() {
        composeTestRule.setContent {
            ConverterCard(
                currencies = listOf(Currency.jpy(), Currency.usd(), Currency("MYR", 0.0)),
                selectedCurrency = Currency.usd(),
                onCurrencyChange = {},
                amount = "1",
                onAmountChange = {},
                convertCurrency = Currency.jpy(),
                onConvertCurrencyChange = {},
                convertedAmount = "149.0",
            )
        }

        compareScreenshot(composeTestRule)
    }
}