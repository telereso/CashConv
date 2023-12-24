package io.telereso.cashconv.app.test.components

import androidx.compose.ui.test.junit4.createComposeRule
import com.karumi.shot.ScreenshotTest
import io.telereso.cashconv.models.Currency
import components.ConvertIcon
import org.junit.Rule
import org.junit.Test
import components.ConverterCard

class ConvertIconTest : ScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testConvertIcon() {
        composeTestRule.setContent {
            ConvertIcon()
        }

        compareScreenshot(composeTestRule)
    }
}