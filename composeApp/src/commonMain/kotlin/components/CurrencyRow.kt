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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.telereso.cashconv.models.Currency

@Composable
fun CurrencyRow(
    modifier: Modifier = Modifier,
    title: String,
    currencies: List<Currency>,
    currency: Currency,
    amount: String?,
    editable: Boolean = true,
    onCurrencyChange: (Currency) -> Unit = {},
    onAmountChange: (String) -> Unit = {},
) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 15.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(400),
                color = Color(0xFF989898),

                ),
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.padding(4.dp))


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            CountryFlag(currency.code)

            Spacer(modifier = Modifier.padding(8.dp))

            CurrencyMenu(
                currencies = currencies,
                selected = currency,
                onSelected = onCurrencyChange
            )

            Spacer(modifier = Modifier.padding(16.dp))

            AmountField(amount, editable, onAmountChange)

        }
    }
}