/*
 * MIT License
 *
 * Copyright (c) 2023 Telereso
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.telereso.cashconv.models.Currency

@Composable
fun ConverterCard(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencyChange: (currency: Currency) -> Unit = {},
    amount: String?,
    onAmountChange: (amount: String) -> Unit = {},
    convertCurrency: Currency,
    onConvertCurrencyChange: (currency: Currency) -> Unit = {},
    convertedAmount: String?,
) {

    LaunchedEffect(AppHelper.refreshCount) {
        AppHelper.cashconvClientManager.fetchRatesIfNeeded()
    }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        shape = RoundedCornerShape(size = 20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
    ) {
        val horizontalPadding = 20.dp

        CurrencyRow(
            modifier = Modifier
                .padding(horizontalPadding),
            title = "Amount",
            currencies = currencies,
            currency = selectedCurrency,
            amount = amount,
            onCurrencyChange = onCurrencyChange,
            onAmountChange = onAmountChange
        )


        Box(
            modifier = Modifier.padding(0.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = horizontalPadding,
                        end = horizontalPadding,
                        top = 0.dp,
                        bottom = 0.dp
                    )
                    .height(1.dp)
                    .background(
                        color = Color(0xFFE7E7EE)
                    )
            )
            ConvertIcon()
        }

        Spacer(modifier = Modifier.padding(2.dp))


        CurrencyRow(
            modifier = Modifier
                .padding(
                    top = 2.dp,
                    start = horizontalPadding,
                    end = horizontalPadding
                ),
            title = "Converted Amount",
            currency = convertCurrency,
            currencies = currencies.filter { it.code != selectedCurrency.code },
            amount = convertedAmount,
            editable = false,
            onCurrencyChange = onConvertCurrencyChange
        )


        Spacer(modifier = Modifier.padding(horizontalPadding - 4.dp))

    }
}