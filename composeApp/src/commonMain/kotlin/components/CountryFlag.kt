package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CountryFlag(
    code: String = "",
    size: Dp = 32.dp,
    modifier: Modifier = Modifier
) {
    if (LocalInspectionMode.current) {
        Box(modifier = modifier.background(Color.Gray).size(size))
        return
    }

    Image(
        painter = painterResource("flags/flag_${code.lowercase()}.png"),
        contentDescription = "country flag",
        contentScale = ContentScale.Fit,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(20))// This ensures a square aspect ratio

    )
}