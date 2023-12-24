package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import org.junit.Rule
import org.junit.Test
import components.ConverterCard
import components.CurrencyItem

class CurrencyItemTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyItem() {
        composeTestRule.setContent {
            CurrencyItem(amount = "1", currency = Currency.usd())
        }

        compareScreenshot(composeTestRule)
    }
}