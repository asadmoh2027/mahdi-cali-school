package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
fun AddGradeDialog(
    students: List<StudentEntity>,
    subjects: List<String>,
    examTerms: List<String>,
    onDismiss: () -> Unit,
    onConfirm: (studentId: Long, studentName: String, gradeClass: String, examTerm: String, subject: String, score: Double) -> Unit
) {
    var selectedStudent by remember { mutableStateOf(students.firstOrNull()) }
    var isStudentDropdownExpanded by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf(subjects.firstOrNull() ?: "Xisaab (Math)") }
    var isSubjectDropdownExpanded by remember { mutableStateOf(false) }
    var selectedTerm by remember { mutableStateOf(examTerms.firstOrNull() ?: "Teeramka 1aad") }
    var isTermDropdownExpanded by remember { mutableStateOf(false) }
    var scoreText by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Gali Natiijo Cusub",
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
                        label = { Text("Ardayga") },
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

                // Select Subject
                ExposedDropdownMenuBox(
                    expanded = isSubjectDropdownExpanded,
                    onExpandedChange = { isSubjectDropdownExpanded = !isSubjectDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedSubject,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Maadada (Subject)") },
                        leadingIcon = { Icon(Icons.Default.Book, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isSubjectDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isSubjectDropdownExpanded,
                        onDismissRequest = { isSubjectDropdownExpanded = false }
                    ) {
                        subjects.forEach { subj ->
                            DropdownMenuItem(
                                text = { Text(subj) },
                                onClick = {
                                    selectedSubject = subj
                                    isSubjectDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Select Term
                ExposedDropdownMenuBox(
                    expanded = isTermDropdownExpanded,
                    onExpandedChange = { isTermDropdownExpanded = !isTermDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = selectedTerm,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Imtixaanka / Teeramka") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isTermDropdownExpanded) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = isTermDropdownExpanded,
                        onDismissRequest = { isTermDropdownExpanded = false }
                    ) {
                        examTerms.forEach { term ->
                            DropdownMenuItem(
                                text = { Text(term) },
                                onClick = {
                                    selectedTerm = term
                                    isTermDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Score textfield
                OutlinedTextField(
                    value = scoreText,
                    onValueChange = {
                        scoreText = it
                        errorMessage = null
                    },
                    label = { Text("Dhibcaha (0 - 100)") },
                    leadingIcon = { Icon(Icons.Default.Grade, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("grade_score_input"),
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
                    val score = scoreText.toDoubleOrNull()
                    if (s == null) {
                        errorMessage = "Fadlan dooro ardayga"
                    } else if (score == null || score < 0 || score > 100) {
                        errorMessage = "Fadlan gali dhibco sax ah (0 ilaa 100)"
                    } else {
                        onConfirm(s.id, s.fullName, s.gradeClass, selectedTerm, selectedSubject, score)
                    }
                },
                modifier = Modifier.testTag("confirm_add_grade_button")
            ) {
                Text("Kaydi Natiijada")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Jooji")
            }
        }
    )
}
