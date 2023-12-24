package components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.telereso.cashconv.models.Currency

@Composable
fun CurrencyItem(
    modifier: Modifier = Modifier,
    currency: Currency,
    amount: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {

        CountryFlag(currency.code)

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            text = currency.code,
            style = TextStyle(
                fontSize = 18.sp,
//                fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(500),
                color = Color(0xFF26278D),
            )
        )

        Spacer(modifier = Modifier.size(16.dp))

        AmountField(amount, editable = false)
    }
}