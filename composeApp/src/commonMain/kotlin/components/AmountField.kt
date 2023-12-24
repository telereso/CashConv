package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AmountField(
    value: String?,
    editable: Boolean = true,
    onValueChange: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFEFEFEF), shape = RoundedCornerShape(size = 7.dp)
            )
            .height(45.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        BasicTextField(
            value = value ?: "",
            onValueChange = onValueChange,
            readOnly = !editable,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            modifier = Modifier.padding(end = 14.dp, start = 4.dp),
            textStyle = TextStyle(
                fontSize = 20.sp,
//                            fontFamily = FontFamily(Font(R.font.roboto)),
                fontWeight = FontWeight(600),
                color = Color(0xFF3C3C3C),
                textAlign = TextAlign.End,
            ),
        )
    }
}