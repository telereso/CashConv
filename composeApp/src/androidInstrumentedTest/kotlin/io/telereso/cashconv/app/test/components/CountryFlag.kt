package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import org.junit.Rule
import org.junit.Test
import components.ConverterCard
import components.CountryFlag

class CountryFlagTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testCountryFlag() {
        composeTestRule.setContent {
            CountryFlag(Currency.jpy().code)
        }

        compareScreenshot(composeTestRule)
    }
}