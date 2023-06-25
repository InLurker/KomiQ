package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.inlurker.komiq.ui.screens.components.AnimatedComponents.RotatingExpandIcon

@Composable
fun DropdownSettings(
    label: String,
    options: List<String>,
    selectedOption: Int,
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit
) {
    var dropdownExpanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable { dropdownExpanded = true }
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = options[selectedOption],
            style = MaterialTheme.typography.labelLarge
        )
        RotatingExpandIcon(isExpanded = dropdownExpanded)
        DropdownMenu(
            expanded = dropdownExpanded,
            onDismissRequest = { dropdownExpanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(index)
                        dropdownExpanded = false
                    }
                )
            }
        }
    }
}