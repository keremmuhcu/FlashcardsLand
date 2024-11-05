package com.keremmuhcu.flashcardsland.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddOrUpdateFlashcardDialog(
    onCloseClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    var termTextField by remember { mutableStateOf("") }
    var definitionTextField by remember { mutableStateOf("") }
    var switchState by remember { mutableStateOf(false) }
    var hardSwitchState by remember { mutableStateOf(false) }
    var examplesTextFields by remember { mutableStateOf(listOf<String>("")) }
    Dialog(
        onDismissRequest = { /* boş alan */ },
        DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Scaffold(
            topBar = {
                AddOrUpdateFlashcardDialogTopBar(
                    onCloseClicked = onCloseClicked,
                    onSaveClicked = onSaveClicked
                )
            },
            bottomBar = { // bottomBar ekleniyor
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = { /* Yeni Kart Ekleme İşlemi */ }
                ) {
                    Text(text = "YENİ KART EKLE", fontFamily = openSansFontFamily, fontSize = 16.sp)
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                FlashcardTermAndDefinitionComponent(
                    modifier = Modifier.fillMaxWidth(),
                    termTextField = termTextField,
                    onTermTextFieldChange = { termTextField = it },
                    definitionTextField = definitionTextField,
                    onDefinitionTextFieldChange = { definitionTextField = it },
                    tfTextStyle = TextStyle(
                        fontFamily = openSansFontFamily,
                        fontSize = 18.sp
                    )
                )

                SwitchesComponent(onSwitchChange = {}, onHardSwitchChange = {})
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    if (switchState) {
                        itemsIndexed(examplesTextFields) { index, example ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                OutlinedTextField(
                                    modifier = Modifier.weight(1f),
                                    value = example,
                                    onValueChange = {
                                        val newTextFields = examplesTextFields.toMutableList()
                                        newTextFields[index] = it
                                        examplesTextFields = newTextFields
                                    },
                                    label = {
                                        Text(
                                            text = "Örnek ${index + 1}",
                                            fontFamily = openSansFontFamily,
                                            fontSize = 16.sp
                                        )
                                    },
                                    textStyle = TextStyle(
                                        fontFamily = openSansFontFamily,
                                        fontSize = 18.sp

                                    )

                                )

                                if (examplesTextFields.size > 1) {
                                    Icon(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .size(28.dp)
                                            .clickable {
                                                examplesTextFields = examplesTextFields
                                                    .toMutableList()
                                                    .apply {
                                                        removeAt(index)
                                                    }
                                            },
                                        imageVector = ImageVector.vectorResource(id = R.drawable.remove_example),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                        }

                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp), contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable {
                                            examplesTextFields = examplesTextFields + ""
                                        },
                                    imageVector = Icons.Rounded.AddCircle,
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SwitchesComponent(
    switchState: Boolean = false,
    hardSwitchState: Boolean = false,
    onSwitchChange: (Boolean) -> Unit,
    onHardSwitchChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Zor",
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Switch(
            checked = hardSwitchState,
            onCheckedChange = { onHardSwitchChange(!hardSwitchState) },
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Örnekler",
            fontFamily = openSansFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Switch(
            checked = switchState,
            onCheckedChange = { onSwitchChange(!switchState) },
        )
    }
}

@Composable
private fun FlashcardTermAndDefinitionComponent(
    modifier: Modifier,
    termTextField: String,
    onTermTextFieldChange: (String) -> Unit,
    definitionTextField: String,
    onDefinitionTextFieldChange: (String) -> Unit,
    tfTextStyle: TextStyle
) {
    OutlinedTextField(
        modifier = modifier,
        value = termTextField,
        onValueChange = { onTermTextFieldChange(it) },
        label = {
            Text(
                text = "Terim",
                fontFamily = openSansFontFamily,
                fontSize = 16.sp
            )
        },
        textStyle = tfTextStyle
    )

    OutlinedTextField(
        modifier = modifier, // fillmaxWidth
        value = definitionTextField,
        onValueChange = { onDefinitionTextFieldChange(it) },
        label = {
            Text(
                text = "Tanım",
                fontFamily = openSansFontFamily,
                fontSize = 16.sp
            )
        },
        textStyle = tfTextStyle
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddOrUpdateFlashcardDialogTopBar(
    onCloseClicked:() -> Unit,
    onSaveClicked:() -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = { onCloseClicked() }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "")
            }
        },
        actions = {
            TextButton(onClick = { onSaveClicked() }) {
                Text(text = "Kaydet", fontFamily = openSansFontFamily, fontSize = 16.sp)
            }
        }
    )
}

@PreviewLightDark
@Composable
private fun AddOrUpdateFlashcardDialogPreview() {
    FlashcardsLandTheme {
        AddOrUpdateFlashcardDialog({},{})
    }

}