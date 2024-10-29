package com.keremmuhcu.flashcardsland.presentation.set_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keremmuhcu.flashcardsland.presentation.components.DeleteDialog
import com.keremmuhcu.flashcardsland.presentation.components.LoadingComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.AddOrEditSetDialog
import com.keremmuhcu.flashcardsland.presentation.set_list.components.EmptySetListComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListCardItemButtonsComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListCardItemContentComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListItemComponent
import com.keremmuhcu.flashcardsland.presentation.set_list.components.SetListTopBarComponent
import com.keremmuhcu.flashcardsland.ui.theme.FlashcardsLandTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetListScreen() {
    val setListViewModel = koinViewModel<SetListViewModel>()
    val state = setListViewModel.state.collectAsStateWithLifecycle()
    var isAddSetDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isUpdateSetDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteDialogOpen by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = { SetListTopBarComponent() },
        floatingActionButton = {
            if(state.value.flashcardSets.isNotEmpty()) {
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
        if(state.value.isLoading) {
            LoadingComponent()
        } else {
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)

            if (state.value.flashcardSets.isEmpty()) {
                EmptySetListComponent(
                    modifier = modifier,
                    onButtonClicked = {
                        isAddSetDialogOpen = true
                    }
                )
            } else {
                LazyColumn(
                    modifier = modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.value.flashcardSets) { set->
                        var isDropdownMenuOpen by remember { mutableStateOf(false) }
                        SetListItemComponent {
                            SetListCardItemContentComponent(
                                set = set,
                                isDropdownMenuOpen = isDropdownMenuOpen,
                                onDropdownClicked = { isDropdownMenuOpen = !isDropdownMenuOpen },
                                onEditClicked = {
                                    setListViewModel.onEvent(SetListEvent.ChangeSelectedSet(set))
                                    setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(set.flashcardSet.title))
                                    isUpdateSetDialogOpen = true
                                    isDropdownMenuOpen = !isDropdownMenuOpen

                                },
                                onDeleteClicked = {
                                    setListViewModel.onEvent(SetListEvent.ChangeSelectedSet(set))
                                    isDeleteDialogOpen = true
                                    isDropdownMenuOpen = !isDropdownMenuOpen
                                }
                            )
                            SetListCardItemButtonsComponent(
                                isStudyButtonEnabled = set.cards.isNotEmpty()
                            )
                        }
                    }
                }
            }
        }


        AddOrEditSetDialog(
            isOpen = isAddSetDialogOpen,
            label = "Set Oluştur",
            setTitle = state.value.setTitleTextField,
            onTitleChange = {
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(it))
            },
            onConfirm = {
                setListViewModel.onEvent(SetListEvent.OnCreateSetButtonClicked)
                isAddSetDialogOpen = false
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(""))
            },
            onCancel = {
                isAddSetDialogOpen = false
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(""))
            }
        )

        AddOrEditSetDialog(
            isOpen = isUpdateSetDialogOpen,
            label = "Seti Düzenle",
            setTitle = state.value.setTitleTextField,
            onTitleChange = {
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(it))
            },
            onConfirm = {
                setListViewModel.onEvent(SetListEvent.OnEditSetButtonClicked)
                isUpdateSetDialogOpen = false
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(""))
            },
            onCancel = {
                isUpdateSetDialogOpen = false
                setListViewModel.onEvent(SetListEvent.OnSetTitleTextFieldChange(""))
            }
        )

        DeleteDialog(
            title = "Set silinecek",
            text = "Kalıcı olarak silinir ve geri alınamaz.",
            isOpen = isDeleteDialogOpen,
            onConfirm = {
                setListViewModel.onEvent(SetListEvent.OnDeleteSetButtonClicked)
                isDeleteDialogOpen = false
            },
            onCancel = {
                isDeleteDialogOpen = false
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun SetListScreenPrev() {
    FlashcardsLandTheme {
        SetListScreen()
    }
}