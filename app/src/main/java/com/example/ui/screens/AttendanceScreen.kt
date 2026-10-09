package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.StudentEntity
import com.example.ui.viewmodel.SchoolViewModel

@Composable
fun AttendanceScreen(
    viewModel: SchoolViewModel
) {
    val selectedDate by viewModel.selectedDate.collectAsState()
    val attendanceClass by viewModel.attendanceClass.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()
    val attendanceRecords by viewModel.attendanceForSelection.collectAsState()

    val classStudents = allStudents.filter { it.gradeClass == attendanceClass }
    val recordMap = attendanceRecords.associateBy { it.studentId }

    val presentCount = attendanceRecords.count { it.status == "PRESENT" }
    val absentCount = attendanceRecords.count { it.status == "ABSENT" }
    val lateCount = attendanceRecords.count { it.status == "LATE" }
    val excusedCount = attendanceRecords.count { it.status == "EXCUSED" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("attendance_screen")
    ) {
        // Date & Class Controls Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarToday,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Taariikhda: $selectedDate",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "Maanta",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Class filter chips
                Text(
                    text = "Dooro Fasalka:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.availableClasses.filter { it != "Dhammaan" }) { cls ->
                        FilterChip(
                            selected = attendanceClass == cls,
                            onClick = { viewModel.setAttendanceClass(cls) },
                            label = { Text(cls) }
                        )
                    }
                }
            }
        }

        // Summary Statistics Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AttendanceStatChip(
                label = "Jooga",
                count = presentCount,
                color = Color(0xFF059669),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatChip(
                label = "Maqan",
                count = absentCount,
                color = Color(0xFFDC2626),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatChip(
                label = "Daahay",
                count = lateCount,
                color = Color(0xFFD97706),
                modifier = Modifier.weight(1f)
            )
            AttendanceStatChip(
                label = "Fasax",
                count = excusedCount,
                color = Color(0xFF2563EB),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Action to mark all present
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ardayda $attendanceClass (${classStudents.size})",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = { viewModel.markClassAllPresent(classStudents) },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("mark_all_present_button")
            ) {
                Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Dhammaan Jooga", fontSize = 12.sp)
            }
        }

        // Student List with Attendance Status Toggles
        if (classStudents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Wax arday ah kuma jiraan fasalka $attendanceClass.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(classStudents, key = { it.id }) { student ->
                    val currentRecord = recordMap[student.id]
                    val currentStatus = currentRecord?.status ?: "UNMARKED"

                    AttendanceStudentItem(
                        student = student,
                        currentStatus = currentStatus,
                        onStatusSelect = { status ->
                            viewModel.markAttendance(student, status)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AttendanceStatChip(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = color.copy(alpha = 0.12f)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    color = color,
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
fun AttendanceStudentItem(
    student: StudentEntity,
    currentStatus: String,
    onStatusSelect: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.fullName.take(2).uppercase(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = student.fullName,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "ID: ${student.rollNumber}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4 Status Buttons: Jooga, Maqan, Daahay, Fasax
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusToggleButton(
                    label = "Jooga",
                    isSelected = currentStatus == "PRESENT",
                    activeColor = Color(0xFF059669),
                    onClick = { onStatusSelect("PRESENT") },
                    modifier = Modifier.weight(1f)
                )

                StatusToggleButton(
                    label = "Maqan",
                    isSelected = currentStatus == "ABSENT",
                    activeColor = Color(0xFFDC2626),
                    onClick = { onStatusSelect("ABSENT") },
                    modifier = Modifier.weight(1f)
                )

                StatusToggleButton(
                    label = "Daahay",
                    isSelected = currentStatus == "LATE",
                    activeColor = Color(0xFFD97706),
                    onClick = { onStatusSelect("LATE") },
                    modifier = Modifier.weight(1f)
                )

                StatusToggleButton(
                    label = "Fasax",
                    isSelected = currentStatus == "EXCUSED",
                    activeColor = Color(0xFF2563EB),
                    onClick = { onStatusSelect("EXCUSED") },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun StatusToggleButton(
    label: String,
    isSelected: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(34.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(6.dp),
        color = if (isSelected) activeColor else MaterialTheme.colorScheme.surfaceVariant,
        contentColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 11.sp
                )
            )
        }
    }
}
