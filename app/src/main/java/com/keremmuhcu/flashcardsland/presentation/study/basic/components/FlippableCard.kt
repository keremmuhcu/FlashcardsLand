package com.keremmuhcu.flashcardsland.presentation.study.basic.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun FlippableCard(
    modifier: Modifier = Modifier,
    englishWord: String,
    turkishWord: String
) {
    var isFlipped by remember { mutableStateOf(false) }
    /*LaunchedEffect(englishWord, turkishWord) {
        isFlipped = false
    }*/
    val rotation = animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600)
    )
    Box(
        modifier = modifier
            .graphicsLayer {
                rotationY = rotation.value
                cameraDistance = 8 * density
            }
            .clickable { isFlipped = !isFlipped },
        contentAlignment = Alignment.Center
    ) {
        if (rotation.value <= 90f) {
            CardFace(
                text = englishWord,
                backgroundColor = MaterialTheme.colorScheme.primary,
                rotationY = 0f // Ön yüz normal görünür
            )
        } else {
            CardFace(
                text = turkishWord,
                backgroundColor = MaterialTheme.colorScheme.tertiary,
                rotationY = 180f // Arka yüz ters görünür
            )
        }
    }
}

@Composable
private fun CardFace(text: String, backgroundColor: Color, rotationY: Float) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.rotationY = rotationY
            },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = openSansFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}