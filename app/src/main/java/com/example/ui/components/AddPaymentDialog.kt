package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.entity.StudentEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentDialog(
    students: List<StudentEntity>,
    onDismiss: () -> Unit,
    onConfirm: (student: StudentEntity, amount: Double, month: String, method: String, receiptNo: String, note: String) -> Unit
) {
    var selectedStudent by remember { mutableStateOf(students.firstOrNull()) }
    var isStudentDropdownExpanded by remember { mutableStateOf(false) }
    var amountText by remember { mutableStateOf("25") }
    var selectedMonth by remember { mutableStateOf("Bisha Janaayo 2025") }
    var selectedMethod by remember { mutableStateOf("EVC Plus") }
    var receiptNumber by remember { mutableStateOf("REC-" + (10000..99999).random()) }
    var note by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val months = listOf(
        "Bisha Janaayo 2025",
        "Bisha Febraayo 2025",
        "Bisha Maarso 2025",
        "Bisha Abriil 2025",
        "Bisha Maajo 2025",
        "Bisha Juun 2025"
    )
    var isMonthDropdownExpanded by remember { mutableStateOf(false) }

    val methods = listOf("EVC Plus", "Zaad Service", "Sahal", "Kaash / Cash")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Qabo Lacag-bixinta Fiiga",
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
                // Select Student
                ExposedDropdownMenuBox(
                    expanded = isStudentDropdownExpanded,
                    onExpandedChange = { isStudentDropdownExpanded = !isStudentDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedStudent?.let { "${it.fullName} (${it.gradeClass})" } ?: "Dooro Arday",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ardayga (Student)") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStudentDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isStudentDropdownExpanded,
                        onDismissRequest = { isStudentDropdownExpanded = false }
                    ) {
                        students.forEach { s ->
                            DropdownMenuItem(
                                text = { Text("${s.fullName} - ${s.gradeClass}") },
                                onClick = {
                                    selectedStudent = s
                                    isStudentDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Amount
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Cadadka Lacagta ($ USD)") },
                    leadingIcon = { Icon(Icons.Default.AttachMoney, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("payment_amount_input"),
                    singleLine = true
                )

                // Month Dropdown
                ExposedDropdownMenuBox(
                    expanded = isMonthDropdownExpanded,
                    onExpandedChange = { isMonthDropdownExpanded = !isMonthDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedMonth,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Bisha (Month)") },
                        leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMonthDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isMonthDropdownExpanded,
                        onDismissRequest = { isMonthDropdownExpanded = false }
                    ) {
                        months.forEach { m ->
                            DropdownMenuItem(
                                text = { Text(m) },
                                onClick = {
                                    selectedMonth = m
                                    isMonthDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Payment Method Chips
                Column {
                    Text(
                        text = "Habka Lacag-bixinta:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        methods.forEach { method ->
                            FilterChip(
                                selected = selectedMethod == method,
                                onClick = { selectedMethod = method },
                                label = { Text(method, style = MaterialTheme.typography.labelSmall) }
                            )
                        }
                    }
                }

                // Receipt #
                OutlinedTextField(
                    value = receiptNumber,
                    onValueChange = { receiptNumber = it },
                    label = { Text("Lambarka Rasiidka (Receipt No)") },
                    leadingIcon = { Icon(Icons.Default.Receipt, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Note
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Faallo / Xusuusin (Ikhtiyaari)") },
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
                    val s = selectedStudent
                    val amt = amountText.toDoubleOrNull()
                    if (s == null) {
                        errorMessage = "Fadlan dooro ardayga"
                    } else if (amt == null || amt <= 0) {
                        errorMessage = "Fadlan gali cadad sax ah"
                    } else {
                        onConfirm(s, amt, selectedMonth, selectedMethod, receiptNumber, note)
                    }
                },
                modifier = Modifier.testTag("confirm_add_payment_button")
            ) {
                Text("Diiwaangeli Lacagta")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Jooji")
            }
        }
    )
}
