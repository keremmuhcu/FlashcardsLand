package com.keremmuhcu.flashcardsland.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun EmptyListScreenComponent(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
    @DrawableRes infoIcon: Int,
    infoText: String,
    buttonText: String,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(48.dp),
            imageVector = ImageVector.vectorResource(infoIcon),
            contentDescription = ""
        )
        Text(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
            text = infoText,
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier.padding(top = 40.dp),
            onClick = { onButtonClicked() }
        ) {
            Text(text = buttonText, fontFamily = openSansFontFamily)
        }
    }
}