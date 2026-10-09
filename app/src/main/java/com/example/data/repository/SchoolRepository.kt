package com.example.data.repository

import com.example.data.dao.SchoolDao
import com.example.data.entity.AnnouncementEntity
import com.example.data.entity.AttendanceEntity
import com.example.data.entity.GradeEntity
import com.example.data.entity.PaymentEntity
import com.example.data.entity.ScheduleEntity
import com.example.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

class SchoolRepository(private val dao: SchoolDao) {

    val allStudents: Flow<List<StudentEntity>> = dao.getAllStudents()
    val studentCount: Flow<Int> = dao.getStudentCount()
    val allPayments: Flow<List<PaymentEntity>> = dao.getAllPayments()
    val totalCollected: Flow<Double?> = dao.getTotalCollectedAmount()
    val allAnnouncements: Flow<List<AnnouncementEntity>> = dao.getAllAnnouncements()
    val allGrades: Flow<List<GradeEntity>> = dao.getAllGrades()

    fun getStudentsByClass(gradeClass: String): Flow<List<StudentEntity>> =
        dao.getStudentsByClass(gradeClass)

    fun searchStudents(query: String): Flow<List<StudentEntity>> =
        dao.searchStudents(query)

    fun getStudentById(id: Long): Flow<StudentEntity?> =
        dao.getStudentById(id)

    suspend fun insertStudent(student: StudentEntity): Long =
        dao.insertStudent(student)

    suspend fun updateStudent(student: StudentEntity) =
        dao.updateStudent(student)

    suspend fun deleteStudent(student: StudentEntity) =
        dao.deleteStudent(student)

    fun getAttendanceByDateAndClass(date: String, gradeClass: String): Flow<List<AttendanceEntity>> =
        dao.getAttendanceByDateAndClass(date, gradeClass)

    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>> =
        dao.getAttendanceForStudent(studentId)

    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>> =
        dao.getAttendanceByDate(date)

    suspend fun recordAttendance(attendance: AttendanceEntity): Long =
        dao.recordAttendance(attendance)

    suspend fun recordAttendanceBatch(records: List<AttendanceEntity>) =
        dao.recordAttendanceBatch(records)

    fun getGradesByClassAndExam(gradeClass: String, examTerm: String): Flow<List<GradeEntity>> =
        dao.getGradesByClassAndExam(gradeClass, examTerm)

    fun getGradesForStudent(studentId: Long): Flow<List<GradeEntity>> =
        dao.getGradesForStudent(studentId)

    suspend fun insertGrade(grade: GradeEntity): Long =
        dao.insertGrade(grade)

    suspend fun deleteGrade(grade: GradeEntity) =
        dao.deleteGrade(grade)

    fun getPaymentsForStudent(studentId: Long): Flow<List<PaymentEntity>> =
        dao.getPaymentsForStudent(studentId)

    suspend fun insertPayment(payment: PaymentEntity): Long =
        dao.insertPayment(payment)

    suspend fun deletePayment(payment: PaymentEntity) =
        dao.deletePayment(payment)

    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long =
        dao.insertAnnouncement(announcement)

    suspend fun deleteAnnouncement(announcement: AnnouncementEntity) =
        dao.deleteAnnouncement(announcement)

    fun getScheduleForClassAndDay(gradeClass: String, dayOfWeek: String): Flow<List<ScheduleEntity>> =
        dao.getScheduleForClassAndDay(gradeClass, dayOfWeek)

    fun getScheduleForClass(gradeClass: String): Flow<List<ScheduleEntity>> =
        dao.getScheduleForClass(gradeClass)
}
