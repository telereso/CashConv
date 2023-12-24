package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import org.junit.Rule
import org.junit.Test
import components.ConverterCard
import components.CurrencyMenu

class CurrencyMenuTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCurrencyMenu() {
        composeTestRule.setContent {
            CurrencyMenu(listOf(Currency.usd(), Currency.jpy()), Currency.jpy())
        }

        compareScreenshot(composeTestRule)
    }
}