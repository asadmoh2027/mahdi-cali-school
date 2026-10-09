package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.entity.StudentEntity
import com.example.ui.components.AddAnnouncementDialog
import com.example.ui.components.AddGradeDialog
import com.example.ui.components.AddPaymentDialog
import com.example.ui.components.AddStudentDialog
import com.example.ui.components.ReportCardDialog
import com.example.ui.components.SchoolHeader
import com.example.ui.components.StudentDetailDialog
import com.example.ui.screens.AttendanceScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.FeesScreen
import com.example.ui.screens.GradesScreen
import com.example.ui.screens.ScheduleAndNoticesScreen
import com.example.ui.screens.StudentsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.SchoolViewModel

enum class SchoolNavDestination(val label: String) {
    DASHBOARD("Dulmar"),
    STUDENTS("Ardayda"),
    ATTENDANCE("Imaansho"),
    GRADES("Natiijo"),
    FEES("Fiiga"),
    SCHEDULE("Jadwalka")
}

class MainActivity : ComponentActivity() {

    private val viewModel: SchoolViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppContent(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: SchoolViewModel) {
    var currentScreen by remember { mutableStateOf(SchoolNavDestination.DASHBOARD) }

    // Dialog States
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showAddPaymentDialog by remember { mutableStateOf(false) }
    var showAddGradeDialog by remember { mutableStateOf(false) }
    var showAddAnnouncementDialog by remember { mutableStateOf(false) }
    var reportCardStudent by remember { mutableStateOf<StudentEntity?>(null) }
    var detailStudent by remember { mutableStateOf<StudentEntity?>(null) }

    val announcements by viewModel.allAnnouncements.collectAsState()
    val allStudents by viewModel.allStudents.collectAsState()

    // Back handling: If on secondary screen, go back to Dashboard
    BackHandler(enabled = currentScreen != SchoolNavDestination.DASHBOARD) {
        currentScreen = SchoolNavDestination.DASHBOARD
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SchoolHeader(
                announcementCount = announcements.size,
                onNotificationClick = {
                    currentScreen = SchoolNavDestination.SCHEDULE
                }
            )
        },
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("school_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = currentScreen == SchoolNavDestination.DASHBOARD,
                    onClick = { currentScreen = SchoolNavDestination.DASHBOARD },
                    icon = { Icon(Icons.Default.Dashboard, contentDescription = "Dulmar") },
                    label = { Text("Dulmar") },
                    modifier = Modifier.testTag("nav_item_dashboard")
                )
                NavigationBarItem(
                    selected = currentScreen == SchoolNavDestination.STUDENTS,
                    onClick = { currentScreen = SchoolNavDestination.STUDENTS },
                    icon = { Icon(Icons.Default.Groups, contentDescription = "Ardayda") },
                    label = { Text("Ardayda") },
                    modifier = Modifier.testTag("nav_item_students")
                )
                NavigationBarItem(
                    selected = currentScreen == SchoolNavDestination.ATTENDANCE,
                    onClick = { currentScreen = SchoolNavDestination.ATTENDANCE },
                    icon = { Icon(Icons.Default.EventAvailable, contentDescription = "Imaansho") },
                    label = { Text("Imaansho") },
                    modifier = Modifier.testTag("nav_item_attendance")
                )
                NavigationBarItem(
                    selected = currentScreen == SchoolNavDestination.GRADES,
                    onClick = { currentScreen = SchoolNavDestination.GRADES },
                    icon = { Icon(Icons.Default.Assessment, contentDescription = "Natiijo") },
                    label = { Text("Natiijo") },
                    modifier = Modifier.testTag("nav_item_grades")
                )
                NavigationBarItem(
                    selected = currentScreen == SchoolNavDestination.FEES,
                    onClick = { currentScreen = SchoolNavDestination.FEES },
                    icon = { Icon(Icons.Default.Paid, contentDescription = "Fiiga") },
                    label = { Text("Fiiga") },
                    modifier = Modifier.testTag("nav_item_fees")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                SchoolNavDestination.DASHBOARD -> {
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToStudents = { currentScreen = SchoolNavDestination.STUDENTS },
                        onNavigateToAttendance = { currentScreen = SchoolNavDestination.ATTENDANCE },
                        onNavigateToGrades = { currentScreen = SchoolNavDestination.GRADES },
                        onNavigateToFees = { currentScreen = SchoolNavDestination.FEES },
                        onNavigateToSchedule = { currentScreen = SchoolNavDestination.SCHEDULE },
                        onOpenAddStudent = { showAddStudentDialog = true },
                        onOpenAddPayment = { showAddPaymentDialog = true }
                    )
                }

                SchoolNavDestination.STUDENTS -> {
                    StudentsScreen(
                        viewModel = viewModel,
                        onStudentClick = { student -> detailStudent = student },
                        onAddStudentClick = { showAddStudentDialog = true }
                    )
                }

                SchoolNavDestination.ATTENDANCE -> {
                    AttendanceScreen(
                        viewModel = viewModel
                    )
                }

                SchoolNavDestination.GRADES -> {
                    GradesScreen(
                        viewModel = viewModel,
                        onAddGradeClick = { showAddGradeDialog = true },
                        onViewReportCard = { student -> reportCardStudent = student }
                    )
                }

                SchoolNavDestination.FEES -> {
                    FeesScreen(
                        viewModel = viewModel,
                        onAddPaymentClick = { showAddPaymentDialog = true }
                    )
                }

                SchoolNavDestination.SCHEDULE -> {
                    ScheduleAndNoticesScreen(
                        viewModel = viewModel,
                        onAddAnnouncementClick = { showAddAnnouncementDialog = true }
                    )
                }
            }
        }
    }

    // Add Student Dialog
    if (showAddStudentDialog) {
        AddStudentDialog(
            availableClasses = viewModel.availableClasses,
            onDismiss = { showAddStudentDialog = false },
            onConfirm = { name, roll, cls, parentName, parentPhone, gender, dob ->
                viewModel.addStudent(name, roll, cls, parentName, parentPhone, gender, dob)
                showAddStudentDialog = false
            }
        )
    }

    // Add Payment Dialog
    if (showAddPaymentDialog) {
        AddPaymentDialog(
            students = allStudents,
            onDismiss = { showAddPaymentDialog = false },
            onConfirm = { student, amt, month, method, receiptNo, note ->
                viewModel.addPayment(student, amt, month, method, receiptNo, note)
                showAddPaymentDialog = false
            }
        )
    }

    // Add Grade Dialog
    if (showAddGradeDialog) {
        AddGradeDialog(
            students = allStudents,
            subjects = viewModel.availableSubjects,
            examTerms = viewModel.examTerms,
            onDismiss = { showAddGradeDialog = false },
            onConfirm = { sId, sName, sClass, term, subj, score ->
                viewModel.addGrade(sId, sName, sClass, term, subj, score)
                showAddGradeDialog = false
            }
        )
    }

    // Add Announcement Dialog
    if (showAddAnnouncementDialog) {
        AddAnnouncementDialog(
            onDismiss = { showAddAnnouncementDialog = false },
            onConfirm = { title, content, category, author ->
                viewModel.addAnnouncement(title, content, category, author)
                showAddAnnouncementDialog = false
            }
        )
    }

    // Student Detail Dialog
    detailStudent?.let { student ->
        StudentDetailDialog(
            student = student,
            viewModel = viewModel,
            onDismiss = { detailStudent = null },
            onViewReportCard = {
                detailStudent = null
                reportCardStudent = it
            },
            onDelete = {
                viewModel.deleteStudent(it)
                detailStudent = null
            }
        )
    }

    // Report Card Dialog
    reportCardStudent?.let { student ->
        ReportCardDialog(
            student = student,
            viewModel = viewModel,
            onDismiss = { reportCardStudent = null }
        )
    }
}
