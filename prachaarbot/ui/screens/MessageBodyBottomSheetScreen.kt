package com.prachaarbot.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MessageBodyBottomSheetScreen(navController: NavHostController) {
    val modalBottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.HalfExpanded)

    ModalBottomSheetLayout(
        modifier = Modifier.background(color = Color.White, shape = RoundedCornerShape(24.dp)),
        sheetState = modalBottomSheetState,
        sheetContent = {
            Box(
                contentAlignment = Alignment.TopEnd,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(

                    onClick = {
                    navController.popBackStack()
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "")
                }

                CircularCheckboxList(items = listOf("1", "2", "3")) {

                }
            }
        }
    ) {
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun CircularCheckboxList(
    items: List<String>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    val listState = rememberLazyListState(Int.MAX_VALUE / 2)

    val selectedCheckbox = mutableStateOf<String?>( null)
    LazyColumn(
        state = listState,
        modifier = modifier
    ) {
        items(Int.MAX_VALUE) { index ->
            val item = items[index % items.size]
            val isChecked = selectedCheckbox.value == item

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedCheckbox.value = item
                        onItemClick(item)
                    }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = {
                        selectedCheckbox.value = if (it) item else null
                        onItemClick(item)
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = item)
            }
        }
    }
}
