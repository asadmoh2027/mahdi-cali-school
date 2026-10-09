package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.SchoolDatabase
import com.example.data.entity.AnnouncementEntity
import com.example.data.entity.AttendanceEntity
import com.example.data.entity.GradeEntity
import com.example.data.entity.PaymentEntity
import com.example.data.entity.ScheduleEntity
import com.example.data.entity.StudentEntity
import com.example.data.repository.SchoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SchoolViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: SchoolRepository

    val availableClasses = listOf(
        "Dhammaan",
        "Form 4A",
        "Form 3A",
        "Form 2B",
        "Form 1A",
        "Fasalka 8aad",
        "Fasalka 7aad"
    )

    val availableSubjects = listOf(
        "Xisaab (Math)",
        "Fiisikis (Physics)",
        "Kimistari (Chemistry)",
        "Bayooloji (Biology)",
        "Af-Soomaali",
        "English",
        "Tarbiyo Islaam",
        "Carabi (Arabic)",
        "Juqraafi (Geography)",
        "Taariikh (History)"
    )

    val examTerms = listOf(
        "Teeramka 1aad",
        "Midterm Exam",
        "Imtixaanka Kama Dambaysta ah"
    )

    val daysOfWeek = listOf("Sabti", "Axad", "Isniin", "Talaado", "Arbaco", "Khamiis")

    // State Holders
    private val _selectedClass = MutableStateFlow("Dhammaan")
    val selectedClass: StateFlow<String> = _selectedClass.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    private val _selectedDate = MutableStateFlow(todayDate)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _attendanceClass = MutableStateFlow("Form 4A")
    val attendanceClass: StateFlow<String> = _attendanceClass.asStateFlow()

    private val _selectedExamTerm = MutableStateFlow("Teeramka 1aad")
    val selectedExamTerm: StateFlow<String> = _selectedExamTerm.asStateFlow()

    private val _selectedExamClass = MutableStateFlow("Form 4A")
    val selectedExamClass: StateFlow<String> = _selectedExamClass.asStateFlow()

    private val _selectedScheduleDay = MutableStateFlow("Sabti")
    val selectedScheduleDay: StateFlow<String> = _selectedScheduleDay.asStateFlow()

    private val _selectedScheduleClass = MutableStateFlow("Form 4A")
    val selectedScheduleClass: StateFlow<String> = _selectedScheduleClass.asStateFlow()

    private val _selectedStudent = MutableStateFlow<StudentEntity?>(null)
    val selectedStudent: StateFlow<StudentEntity?> = _selectedStudent.asStateFlow()

    init {
        val db = SchoolDatabase.getDatabase(application, viewModelScope)
        repository = SchoolRepository(db.schoolDao())
    }

    // Repository flows
    val allStudents: StateFlow<List<StudentEntity>> = repository.allStudents
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredStudents: StateFlow<List<StudentEntity>> = combine(
        repository.allStudents,
        _selectedClass,
        _searchQuery
    ) { students, filterClass, query ->
        students.filter { student ->
            val matchesClass = if (filterClass == "Dhammaan") true else student.gradeClass.equals(filterClass, ignoreCase = true)
            val matchesQuery = query.isBlank() ||
                    student.fullName.contains(query, ignoreCase = true) ||
                    student.rollNumber.contains(query, ignoreCase = true) ||
                    student.parentPhone.contains(query, ignoreCase = true)
            matchesClass && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val attendanceForSelection: StateFlow<List<AttendanceEntity>> = combine(
        _selectedDate,
        _attendanceClass
    ) { date, gradeClass ->
        Pair(date, gradeClass)
    }.flatMapLatest { (date, gradeClass) ->
        repository.getAttendanceByDateAndClass(date, gradeClass)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayAllAttendance: StateFlow<List<AttendanceEntity>> = repository.getAttendanceByDate(todayDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val gradesForClassAndExam: StateFlow<List<GradeEntity>> = combine(
        _selectedExamClass,
        _selectedExamTerm
    ) { gradeClass, term ->
        Pair(gradeClass, term)
    }.flatMapLatest { (gradeClass, term) ->
        repository.getGradesByClassAndExam(gradeClass, term)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allGrades: StateFlow<List<GradeEntity>> = repository.allGrades
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allPayments: StateFlow<List<PaymentEntity>> = repository.allPayments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCollected: StateFlow<Double?> = repository.totalCollected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val allAnnouncements: StateFlow<List<AnnouncementEntity>> = repository.allAnnouncements
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentSchedule: StateFlow<List<ScheduleEntity>> = combine(
        _selectedScheduleClass,
        _selectedScheduleDay
    ) { gradeClass, day ->
        Pair(gradeClass, day)
    }.flatMapLatest { (gradeClass, day) ->
        repository.getScheduleForClassAndDay(gradeClass, day)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selection Setters
    fun setSelectedClass(cls: String) { _selectedClass.value = cls }
    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setSelectedDate(date: String) { _selectedDate.value = date }
    fun setAttendanceClass(cls: String) { _attendanceClass.value = cls }
    fun setSelectedExamTerm(term: String) { _selectedExamTerm.value = term }
    fun setSelectedExamClass(cls: String) { _selectedExamClass.value = cls }
    fun setSelectedScheduleDay(day: String) { _selectedScheduleDay.value = day }
    fun setSelectedScheduleClass(cls: String) { _selectedScheduleClass.value = cls }
    fun setSelectedStudent(student: StudentEntity?) { _selectedStudent.value = student }

    // Actions
    fun addStudent(
        fullName: String,
        rollNumber: String,
        gradeClass: String,
        parentName: String,
        parentPhone: String,
        gender: String,
        dateOfBirth: String
    ) {
        viewModelScope.launch {
            val student = StudentEntity(
                rollNumber = rollNumber.ifBlank { "MCS-" + (1000..9999).random() },
                fullName = fullName.trim(),
                gradeClass = gradeClass,
                parentName = parentName.trim(),
                parentPhone = parentPhone.trim(),
                gender = gender,
                dateOfBirth = dateOfBirth,
                admissionDate = todayDate,
                status = "ACTIVE"
            )
            repository.insertStudent(student)
        }
    }

    fun updateStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.updateStudent(student)
            if (_selectedStudent.value?.id == student.id) {
                _selectedStudent.value = student
            }
        }
    }

    fun deleteStudent(student: StudentEntity) {
        viewModelScope.launch {
            repository.deleteStudent(student)
            if (_selectedStudent.value?.id == student.id) {
                _selectedStudent.value = null
            }
        }
    }

    fun markAttendance(student: StudentEntity, status: String, notes: String = "") {
        viewModelScope.launch {
            val record = AttendanceEntity(
                studentId = student.id,
                studentName = student.fullName,
                gradeClass = student.gradeClass,
                date = _selectedDate.value,
                status = status,
                notes = notes
            )
            repository.recordAttendance(record)
        }
    }

    fun markClassAllPresent(students: List<StudentEntity>) {
        viewModelScope.launch {
            val records = students.map { student ->
                AttendanceEntity(
                    studentId = student.id,
                    studentName = student.fullName,
                    gradeClass = student.gradeClass,
                    date = _selectedDate.value,
                    status = "PRESENT",
                    notes = ""
                )
            }
            repository.recordAttendanceBatch(records)
        }
    }

    fun addGrade(
        studentId: Long,
        studentName: String,
        gradeClass: String,
        examTerm: String,
        subject: String,
        score: Double
    ) {
        viewModelScope.launch {
            val grade = GradeEntity(
                studentId = studentId,
                studentName = studentName,
                gradeClass = gradeClass,
                examTerm = examTerm,
                subject = subject,
                score = score.coerceIn(0.0, 100.0),
                recordedDate = todayDate
            )
            repository.insertGrade(grade)
        }
    }

    fun deleteGrade(grade: GradeEntity) {
        viewModelScope.launch {
            repository.deleteGrade(grade)
        }
    }

    fun addPayment(
        student: StudentEntity,
        amount: Double,
        monthCovered: String,
        paymentMethod: String,
        receiptNo: String,
        note: String = ""
    ) {
        viewModelScope.launch {
            val payment = PaymentEntity(
                studentId = student.id,
                studentName = student.fullName,
                rollNumber = student.rollNumber,
                gradeClass = student.gradeClass,
                receiptNumber = receiptNo.ifBlank { "REC-" + (10000..99999).random() },
                amount = amount,
                monthCovered = monthCovered,
                paymentDate = todayDate,
                paymentMethod = paymentMethod,
                status = "PAID",
                note = note
            )
            repository.insertPayment(payment)
        }
    }

    fun deletePayment(payment: PaymentEntity) {
        viewModelScope.launch {
            repository.deletePayment(payment)
        }
    }

    fun addAnnouncement(title: String, content: String, category: String, author: String) {
        viewModelScope.launch {
            val announcement = AnnouncementEntity(
                title = title.trim(),
                content = content.trim(),
                category = category,
                postedDate = todayDate,
                author = author.ifBlank { "Maamulka Dugsiga Mahdi Cali" }
            )
            repository.insertAnnouncement(announcement)
        }
    }

    fun deleteAnnouncement(announcement: AnnouncementEntity) {
        viewModelScope.launch {
            repository.deleteAnnouncement(announcement)
        }
    }

    fun getStudentGrades(studentId: Long): StateFlow<List<GradeEntity>> {
        return repository.getGradesForStudent(studentId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getStudentPayments(studentId: Long): StateFlow<List<PaymentEntity>> {
        return repository.getPaymentsForStudent(studentId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getStudentAttendance(studentId: Long): StateFlow<List<AttendanceEntity>> {
        return repository.getAttendanceForStudent(studentId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}
