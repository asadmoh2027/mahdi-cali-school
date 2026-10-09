package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AddAnnouncementDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, content: String, category: String, author: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("MUHIIM") }
    var author by remember { mutableStateOf("Maamulka Dugsiga Mahdi Cali") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val categories = listOf("MUHIIM", "IMTIXAAN", "FASAX", "GUUD")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Daabac Ogeysiis Cusub",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        errorMessage = null
                    },
                    label = { Text("Cinwaanka Ogeysiiska") },
                    leadingIcon = { Icon(Icons.Default.Campaign, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("announcement_title_input"),
                    singleLine = true
                )

                Column {
                    Text(
                        text = "Nooca Ogeysiiska:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        categories.forEach { cat ->
                            FilterChip(
                                selected = category == cat,
                                onClick = { category = cat },
                                label = { Text(cat, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = content,
                    onValueChange = {
                        content = it
                        errorMessage = null
                    },
                    label = { Text("Qoraalka / Faahfaahinta") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("announcement_content_input"),
                    minLines = 3,
                    maxLines = 5
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Qoraha (Author)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        errorMessage = "Fadlan gali cinwaanka"
                    } else if (content.isBlank()) {
                        errorMessage = "Fadlan gali qoraalka ogeysiiska"
                    } else {
                        onConfirm(title, content, category, author)
                    }
                },
                modifier = Modifier.testTag("confirm_add_announcement_button")
            ) {
                Text("Daabac Ogeysiiska")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Jooji")
            }
        }
    )
}
