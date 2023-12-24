package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
fun ConvertIcon(
    size: Dp = 45.dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .size(size)
            .background(
                color = Color(0xFF26278D), shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (LocalInspectionMode.current) {
            Box(modifier = modifier.background(Color.Gray).size(size))
            return
        }
        Image(
            painter = painterResource(
                "ic_convert.xml"
            ),
            contentDescription = "country flag",
            modifier = modifier
                .size(size.div(1.7f))
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}