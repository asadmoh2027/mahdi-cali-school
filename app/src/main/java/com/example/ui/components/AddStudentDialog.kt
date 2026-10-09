package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentDialog(
    availableClasses: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (
        fullName: String,
        rollNumber: String,
        gradeClass: String,
        parentName: String,
        parentPhone: String,
        gender: String,
        dob: String
    ) -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var rollNumber by remember { mutableStateOf("") }
    var selectedClass by remember {
        mutableStateOf(availableClasses.firstOrNull { it != "Dhammaan" } ?: "Form 4A")
    }
    var parentName by remember { mutableStateOf("") }
    var parentPhone by remember { mutableStateOf("+252 ") }
    var gender by remember { mutableStateOf("M") }
    var dob by remember { mutableStateOf("2008-01-15") }
    var isClassDropdownExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val classesToSelect = availableClasses.filter { it != "Dhammaan" }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Diiwaangeli Arday Cusub",
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
                Text(
                    text = "Gali xogta ardayga cusub ee ku biiraya Dugsiga Mahdi Cali.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        errorMessage = null
                    },
                    label = { Text("Magaca Ardayga oo Saddexan") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_name_input"),
                    singleLine = true
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = rollNumber,
                        onValueChange = { rollNumber = it },
                        label = { Text("Roll No / ID") },
                        placeholder = { Text("MCS-2024-...") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("student_roll_input"),
                        singleLine = true
                    )

                    // Gender selector
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Jinsiga:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            FilterChip(
                                selected = gender == "M",
                                onClick = { gender = "M" },
                                label = { Text("Lab (M)") }
                            )
                            FilterChip(
                                selected = gender == "F",
                                onClick = { gender = "F" },
                                label = { Text("Dheddig (F)") }
                            )
                        }
                    }
                }

                // Class Dropdown
                ExposedDropdownMenuBox(
                    expanded = isClassDropdownExpanded,
                    onExpandedChange = { isClassDropdownExpanded = !isClassDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedClass,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fasalka (Class)") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClassDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isClassDropdownExpanded,
                        onDismissRequest = { isClassDropdownExpanded = false }
                    ) {
                        classesToSelect.forEach { cls ->
                            DropdownMenuItem(
                                text = { Text(cls) },
                                onClick = {
                                    selectedClass = cls
                                    isClassDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = parentName,
                    onValueChange = { parentName = it },
                    label = { Text("Magaca Waalidka / Mas'uulka") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_parent_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = parentPhone,
                    onValueChange = { parentPhone = it },
                    label = { Text("Taleefanka Waalidka") },
                    placeholder = { Text("+252 61 XXXXXXX") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_parent_phone_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = dob,
                    onValueChange = { dob = it },
                    label = { Text("Taariikhda Dhalashada (YYYY-MM-DD)") },
                    leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
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
                    if (fullName.isBlank()) {
                        errorMessage = "Fadlan gali magaca ardayga"
                    } else {
                        onConfirm(
                            fullName,
                            rollNumber,
                            selectedClass,
                            parentName.ifBlank { "Waalidka Ardayga" },
                            parentPhone,
                            gender,
                            dob
                        )
                    }
                },
                modifier = Modifier.testTag("confirm_add_student_button")
            ) {
                Text("Diiwaangeli")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Jooji")
            }
        }
    )
}
