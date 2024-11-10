package com.keremmuhcu.flashcardsland.presentation.study.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyTopBar(
    currentCardIsHard: Boolean,
    currentCardIndex: Int,
    flashcardCount: Int,
    isFinish: Boolean,
    onCloseIconClicked: () -> Unit,
    onFavoriteButtonClicked: () -> Unit,
) {
    var goBackAlertDialogControl by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            if (!isFinish) {
                Text(
                    text = "${currentCardIndex + 1}/$flashcardCount",
                    fontFamily = openSansFontFamily
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { goBackAlertDialogControl = true }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        },
        actions = {
            if (!isFinish) {
                IconButton(onClick = { onFavoriteButtonClicked() }) {
                    Icon(
                        imageVector = if (currentCardIsHard) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "",
                        tint = if (currentCardIsHard) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    )

    CustomAlertDialog(
        title = "Çalışma sonlandırılacak",
        text = "Şimdiye kadarki olan ilerlemeniz kaydedilecek.",
        isOpen = goBackAlertDialogControl,
        onConfirm = {
            goBackAlertDialogControl = false
            onCloseIconClicked()
        },
        onCancel = {
            goBackAlertDialogControl = false
        }
    )
}