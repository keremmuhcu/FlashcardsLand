package com.keremmuhcu.flashcardsland.presentation.set_list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.presentation.components.SwitchRowWithText
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@Composable
fun ChooseStudyTypeDialog(
    isOpen: Boolean,
    workHard: Boolean,
    workHardSwitchClicked: () -> Unit,
    onBasicStudyClicked: () -> Unit,
    onMultipleStudyClicked: () -> Unit,
    onSettingsIconClicked:() -> Unit,
    onCancel: () -> Unit
) {
    if (isOpen) {
        AlertDialog(
            icon = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { onCancel() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "")
                    }

                    IconButton(onClick = { onSettingsIconClicked() }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.cards_settings), contentDescription = "")
                    }
                }
            },
            title = {
                SwitchRowWithText(text = "Sadece zorlar", switchState = workHard, switchStateChanged = workHardSwitchClicked)
            },
            text = {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onBasicStudyClicked() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Image(
                            modifier = Modifier.size(75.dp),
                            painter = painterResource(id = R.drawable.basic_study),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Basit Çalışma",
                            fontFamily = openSansFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                    HorizontalDivider()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMultipleStudyClicked() },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                    ) {
                        Image(
                            modifier = Modifier.size(75.dp),
                            painter = painterResource(id = R.drawable.multiple_study),
                            contentDescription = ""
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Çoktan Seçmeli",
                            fontFamily = openSansFontFamily,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            onDismissRequest = { onCancel() },
            confirmButton = { /*TODO*/ },
            shape = RectangleShape
        )
    }
}