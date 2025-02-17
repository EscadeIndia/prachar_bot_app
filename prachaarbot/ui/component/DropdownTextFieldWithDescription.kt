package com.prachaarbot.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.prachaarbot.ui.theme.Black
import com.prachaarbot.ui.theme.Monstserrat

@Composable
internal fun DropdownTextFieldWithDescription(
    options: List<String>, onOptionSelected: (option: String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(options.first()) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp).clickable {
                expanded = !expanded
            }
    ) {
        OutlinedTextField(
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                disabledTextColor = Black,
                disabledTrailingIconColor = Black
            ),
            shape = RoundedCornerShape(8.dp),
            value = selectedOption,
            onValueChange = { selectedOption = it },
            label = {
                Text(
                    "Message Body",
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    fontFamily = Monstserrat,
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFieldSize = coordinates.size.toSize()
                }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(textFieldSize.width.dp)
                .background(color = Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = {
                        Text(
                            option,
                            color = Black

                        )
                    },
                    onClick = {
                        selectedOption = option
                        onOptionSelected.invoke(option)
                        expanded = false
                    }
                )
            }
        }
    }

}
