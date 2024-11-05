package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.R
import com.keremmuhcu.flashcardsland.presentation.components.CustomAlertDialog
import com.keremmuhcu.flashcardsland.presentation.components.EmptyListScreenComponent
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.AddOrEditSetDialog
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListCardItemButtonsComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListCardItemContentComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListItemComponent
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import com.keremmuhcu.flashcardsland.ui.theme.openSansFontFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetListScreen(
    setListViewModel: SetListViewModel = koinViewModel<SetListViewModel>(),
    navigateToAddOrEditFlashcardScreen: (Int) -> Unit,
    navigateToFlashcardsScreen: (Int, String) -> Unit,
    navigateToBasicStudyScreen: (Int) -> Unit
) {
    val state by setListViewModel.state.collectAsStateWithLifecycle()

    var isAddSetDialogOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { SetListTopBarComponent() },
        floatingActionButton = {
            if(state.flashcardSets.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        isAddSetDialogOpen = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = ""
                    )
                }
            }
        }
    ) { innerPadding ->
        if(state.isLoading) {
            LoadingComponent()
        } else {
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            if (state.flashcardSets.isEmpty()) {
                EmptyListScreenComponent(
                    modifier = modifier,
                    onButtonClicked = {
                        isAddSetDialogOpen = true
                    },
                    infoIcon = R.drawable.no_sets,
                    infoText = "Henüz bir set oluşturmadınız\nDaha verimli çalışmak için \nhemen başla.",
                    buttonText = "Set oluştur"
                )
            } else {
                SetListScreenContent(
                    modifier = modifier,
                    state = state,
                    onEvent = setListViewModel::onEvent,
                    navigateToFlashcardsScreen = navigateToFlashcardsScreen,
                    navigateToAddOrEditFlashcardScreen = navigateToAddOrEditFlashcardScreen,
                    navigateToBasicStudyScreen = navigateToBasicStudyScreen
                )
            }
            AddOrEditSetDialog(
                isOpen = isAddSetDialogOpen,
                label = "Set Oluştur",
                setTitle = state.setTitleTextField,
                onTitleChange = {
                    setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(it))
                },
                onConfirm = {
                    setListViewModel.onEvent(SetListEvent.OnCreateSetButtonClicked)
                    isAddSetDialogOpen = false
                    setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(TextFieldValue("")))
                },
                onCancel = {
                    isAddSetDialogOpen = false
                    setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(TextFieldValue("")))
                }
            )
        }
    }
}

@Composable
fun SetListScreenContent(
    modifier: Modifier = Modifier,
    state: SetListState,
    onEvent: (SetListEvent) -> Unit,
    navigateToFlashcardsScreen: (Int, String) -> Unit,
    navigateToAddOrEditFlashcardScreen: (Int) -> Unit,
    navigateToBasicStudyScreen: (Int) -> Unit
) {
    var isUpdateSetDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.flashcardSets) { set->
            var isDropdownMenuOpen by rememberSaveable { mutableStateOf(false) }
            SetListItemComponent(
                setItemClicked = {
                    navigateToFlashcardsScreen(set.flashcardSet.setId!!, set.flashcardSet.title)
                },
                content = {
                    SetListCardItemContentComponent(
                        set = set,
                        isDropdownMenuOpen = isDropdownMenuOpen,
                        onDropdownClicked = { isDropdownMenuOpen = !isDropdownMenuOpen },
                        onEditClicked = {
                            onEvent(SetListEvent.ChangeSelectedSet(set))
                            onEvent(SetListEvent.OnSetTitleTextFieldChange(
                                TextFieldValue(set.flashcardSet.title, TextRange(set.flashcardSet.title.length))
                            ))
                            isUpdateSetDialogOpen = true
                            isDropdownMenuOpen = !isDropdownMenuOpen

                        },
                        onDeleteClicked = {
                            onEvent(SetListEvent.ChangeSelectedSet(set))
                            isDeleteDialogOpen = true
                            isDropdownMenuOpen = !isDropdownMenuOpen
                        }
                    )
                    SetListCardItemButtonsComponent(
                        isStudyButtonEnabled = set.cards.size > 4,
                        addCardButtonClicked = {
                            navigateToAddOrEditFlashcardScreen(set.flashcardSet.setId!!)
                        },
                        studyButtonClicked = {
                            navigateToBasicStudyScreen(set.flashcardSet.setId!!)
                        }
                    )
                }
            )
        }
    }



    AddOrEditSetDialog(
        isOpen = isUpdateSetDialogOpen,
        label = "Seti Düzenle",
        setTitle = state.setTitleTextField,
        onTitleChange = {
            onEvent(SetListEvent.OnSetTitleTextFieldChange(it))
        },
        onConfirm = {
            onEvent(SetListEvent.OnEditSetButtonClicked)
            isUpdateSetDialogOpen = false
            onEvent(SetListEvent.OnSetTitleTextFieldChange(TextFieldValue("")))
        },
        onCancel = {
            isUpdateSetDialogOpen = false
            onEvent(SetListEvent.OnSetTitleTextFieldChange(TextFieldValue("")))
        }
    )

    CustomAlertDialog(
        title = "Set silinecek",
        text = "İçindeki tüm kartlarla beraber silinir. Bu işlem geri alınamaz.",
        isOpen = isDeleteDialogOpen,
        onConfirm = {
            onEvent(SetListEvent.OnDeleteSetButtonClicked)
            isDeleteDialogOpen = false
        },
        onCancel = {
            isDeleteDialogOpen = false
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetListTopBarComponent() {
    TopAppBar(
        title = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Setlerim",
                fontFamily = openSansFontFamily,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@PreviewLightDark
@Composable
private fun SetListScreenPrev() {
    FlashcardsLandTheme {
        //SetListScreen()
    }
}
