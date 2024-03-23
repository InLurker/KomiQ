package com.inlurker.komiq.ui.screens.components.SettingComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.inlurker.komiq.model.data.repository.ComicLanguageSetting

@Composable
fun FilterLibrary(
    selectedLanguage: List<ComicLanguageSetting>,
    onApplySettings: (List<ComicLanguageSetting>) -> Unit
) {
    var tempSelectedLanguage by remember {
        mutableStateOf(selectedLanguage)
    }

    Column {
        LazyColumn(
            userScrollEnabled = true,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .height(260.dp)
        ) {
            items(ComicLanguageSetting.asList()) { language ->
                val checked = remember {
                    derivedStateOf { tempSelectedLanguage.contains(language) }
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable {
                            tempSelectedLanguage = if (checked.value) {
                                tempSelectedLanguage.minus(language)
                            } else {
                                tempSelectedLanguage.plus(language)
                            }
                        }
                        .fillMaxWidth()
                ) {
                    Checkbox(
                        checked = checked.value,
                        onCheckedChange = {
                            tempSelectedLanguage = if (checked.value) {
                                tempSelectedLanguage.minus(language)
                            } else {
                                tempSelectedLanguage.plus(language)
                            }
                        }
                    )
                    Text(
                        text = language.languageDisplayName(), // Assuming ComicLanguageSetting has a 'name' field for display purposes
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Row(modifier = Modifier.padding(top = 16.dp)) {
            Spacer(Modifier.weight(1f))
            Button(
                onClick = {
                    onApplySettings(tempSelectedLanguage)
                }
            ) {
                Text("Apply")
            }
        }
    }
}
