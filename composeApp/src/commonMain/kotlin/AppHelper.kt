@file:OptIn(ExperimentalResourceApi::class)

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.telereso.cashconv.client.CashconvClientManager
import io.telereso.cashconv.client.convert
import io.telereso.cashconv.models.Amount
import components.ConverterCard
import components.CurrencyItem
import io.telereso.kmp.core.Config
import io.telereso.kmp.core.CoreClient
import io.telereso.kmp.core.Task
import io.telereso.kmp.core.Utils.launchPeriodicAsync
import io.telereso.kmp.core.models.ClientException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import org.jetbrains.compose.resources.ExperimentalResourceApi
import kotlin.time.DurationUnit
import kotlin.time.toDuration


internal object AppHelper {
    var refreshCount = 0
    val cashconvClientManager
        get() =
            CashconvClientManager.getInstanceOrNull()
                ?: CashconvClientManager.client {
                    withConfig(
                        Config.builder("CashConv", "1.0.0")
                            .shouldLogHttpRequests(true)
                            .build()
                    )
                    withFloatingDigit(3)
                }

}


@Composable
fun App() {
    LaunchedEffect(Unit) {
        CoreClient.debugLogger()
        ClientException.listener = {
            it.printStackTrace()
        }
        Task.execute {
            launchPeriodicAsync(31.toDuration(DurationUnit.MINUTES)) {
                AppHelper.refreshCount++
            }
        }
    }
    MaterialTheme {
        Scaffold {
            Column(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xEAEAFE), Color(0xFFFFFF)),
                            end = Offset(0f, Float.POSITIVE_INFINITY),
                            start = Offset(Float.POSITIVE_INFINITY, 0f),
                        )
                    )
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val (amountFlow, currenciesFlow) = remember {
                    val amountFlow = MutableStateFlow(Amount.default())

                    val currenciesFlow = AppHelper.cashconvClientManager
                        .getCurrenciesRatesFlow()
                        .combine(amountFlow) { currencies, amount ->
                            if (amount.value.isEmpty())
                                currencies
                            else
                                currencies.map { currency ->
                                    currency.copy(
                                        converted = AppHelper.cashconvClientManager.convert(
                                            amount.value,
                                            amount.currency,
                                            currency
                                        )
                                    )
                                }
                        }

                    amountFlow to currenciesFlow
                }

                val amount by amountFlow.collectAsState(Amount.default())

                val currencies by currenciesFlow.collectAsState(emptyList())

                if (amount.currency.emptyRate())
                    currencies.firstOrNull { it.code == amount.currency.code }?.let {
                        amountFlow.value = amount.copy(currency = it)
                    }

                if (amount.convertCurrency.emptyRate())
                    currencies.firstOrNull { it.code == amount.convertCurrency.code }?.let {
                        amountFlow.value = amount.copy(convertCurrency = it)
                    }

                Spacer(
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    text = "Currency Converter", style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight(700),
                        color = Color(0xFF1F2261),
                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Select Currencies and input amount", style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF808080),

                        textAlign = TextAlign.Center,
                    )
                )

                Spacer(
                    modifier = Modifier.padding(16.dp)
                )

                ConverterCard(
                    currencies = currencies,
                    selectedCurrency = amount.currency,
                    onCurrencyChange = { newCurrency ->
                        amountFlow.value = amount
                            .copy(currency = newCurrency)
                            .convert()
                    },
                    amount = amount.value,
                    onAmountChange = { newAmount ->
                        amountFlow.value = amount
                            .copy(value = newAmount)
                            .convert()
                    },
                    convertCurrency = amount.convertCurrency,
                    onConvertCurrencyChange = { newCurrency ->
                        amountFlow.value = amount
                            .copy(convertCurrency = newCurrency)
                            .convert()
                    },
                    convertedAmount = amount.convertedValue,
                )

                Spacer(modifier = Modifier.padding(8.dp))

                Text(
                    text = "Other Currencies",
                    style = TextStyle(
                        fontSize = 15.sp,
//                        fontFamily = FontFamily(Font(R.font.roboto)),
                        fontWeight = FontWeight(400),
                        color = Color(0xFF989898),

                        ),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.padding(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                ) {

                    items(
                        currencies
                    ) { currency ->

                        CurrencyItem(currency = currency, amount = currency.converted)

                        Spacer(modifier = Modifier.size(16.dp))

                    }

                }
            }
        }
    }
}