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