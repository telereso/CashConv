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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CurrencyMenu(
    currencies: List<Currency>,
    selected: Currency,
    onSelected: (Currency) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf(selected) }

    Row(
        modifier = Modifier.clickable {
            expanded = true
        },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = selectedValue.code,
            style = TextStyle(
//                platformStyle = PlatformTextStyle(
//                    includeFontPadding = false
//                ),
                fontSize = 18.sp,
//                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(500),
                color = Color(0xFF26278D),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .width(43.dp)
        )

        IconButton(onClick = {
            expanded = true
        }) {
            Icon(
                Icons.Default.KeyboardArrowDown,
                contentDescription = "Open Menu"
            )
        }

        // menu
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            currencies.forEach {
                DropdownMenuItem(text = {
                    Text(it.code)
                }, onClick = {
                    expanded = false
                    selectedValue = it
                    onSelected(it)
                }, leadingIcon = {
                    CountryFlag(
                        code = it.code,
                        size = 16.dp
                    )
                })
            }
        }
    }
}
