package com.keremmuhcu.flashcardsland.presentation.study.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun StudyResults(
    modifier: Modifier = Modifier,
    wrongs: String,
    corrects: String,
    remainingCards: String,
    startNextRound: () -> Unit,
    repeatCurrentRound: () -> Unit,
    finishedButtonClicked:() -> Unit
) {
    val composition1 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration_lottie))
    val composition2 by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.successful_lottie))
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.height(100.dp),
            contentAlignment = Alignment.Center
        ) {
            if (wrongs.toInt() == 0) LottieAnimation(composition = composition1)
            LottieAnimation(composition = composition2)
        }
        Text(
            text = "Çalışma tamamlandı!",
            fontFamily = openSansFontFamily,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )

        HorizontalDivider()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 20.dp),
        ) {
            Column(
                modifier = Modifier.padding(all = 20.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ResultTextRow(
                    text = if (wrongs.toInt() == 0) "Tebrikler! Hepsi Doğru: " else "Doğrular: ",
                    result = corrects,
                    color = MaterialTheme.colorScheme.primary
                )
                if (wrongs.toInt() > 0) {
                    ResultTextRow(
                        text = "Yanlışlar: ",
                        result = wrongs,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                ResultTextRow(
                    text = "Kalan kartlar: ",
                    result = remainingCards,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
        Spacer(
            modifier = Modifier.weight(1f)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { startNextRound() }
                ) {
                    Text(text = "SONRAKİ TUR", fontFamily = openSansFontFamily)
                }
                OutlinedButton(
                    modifier = Modifier.weight(0.5f),
                    onClick = { repeatCurrentRound() }
                ) {
                    Text(text = "TURU TEKRARLA", fontFamily = openSansFontFamily)
                }
            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { finishedButtonClicked() }
            ) {
                Text(text = "BİTİR", fontFamily = openSansFontFamily)
            }
        }
    }
}

@Composable
private fun ResultTextRow(
    text: String,
    result: String,
    color: Color = Color.Unspecified
) {
    val textStyle = TextStyle(
        fontSize = 22.sp,
        fontFamily = openSansFontFamily,
    )
    Row {
        Text(
            text = text,
            style = textStyle,
            color = color
        )
        Text(
            text = result,
            style = textStyle
        )
    }
}