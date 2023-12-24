package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import components.AmountField
import org.junit.Rule
import org.junit.Test
import components.ConverterCard

class AmountFieldTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAmountField() {
        composeTestRule.setContent {
            AmountField("1")
        }

        compareScreenshot(composeTestRule)
    }
}