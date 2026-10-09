package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.AnnouncementEntity
import com.example.data.entity.AttendanceEntity
import com.example.data.entity.GradeEntity
import com.example.data.entity.PaymentEntity
import com.example.data.entity.ScheduleEntity
import com.example.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {

    // --- STUDENTS ---
    @Query("SELECT * FROM students ORDER BY fullName ASC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE gradeClass = :gradeClass ORDER BY fullName ASC")
    fun getStudentsByClass(gradeClass: String): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE id = :studentId LIMIT 1")
    fun getStudentById(studentId: Long): Flow<StudentEntity?>

    @Query("SELECT * FROM students WHERE fullName LIKE '%' || :query || '%' OR rollNumber LIKE '%' || :query || '%' OR parentPhone LIKE '%' || :query || '%'")
    fun searchStudents(query: String): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudents(students: List<StudentEntity>)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("SELECT COUNT(*) FROM students")
    fun getStudentCount(): Flow<Int>

    // --- ATTENDANCE ---
    @Query("SELECT * FROM attendance WHERE date = :date AND gradeClass = :gradeClass")
    fun getAttendanceByDateAndClass(date: String, gradeClass: String): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE studentId = :studentId ORDER BY date DESC")
    fun getAttendanceForStudent(studentId: Long): Flow<List<AttendanceEntity>>

    @Query("SELECT * FROM attendance WHERE date = :date")
    fun getAttendanceByDate(date: String): Flow<List<AttendanceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordAttendance(attendance: AttendanceEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordAttendanceBatch(records: List<AttendanceEntity>)

    // --- GRADES ---
    @Query("SELECT * FROM grades WHERE gradeClass = :gradeClass AND examTerm = :examTerm ORDER BY studentName ASC")
    fun getGradesByClassAndExam(gradeClass: String, examTerm: String): Flow<List<GradeEntity>>

    @Query("SELECT * FROM grades WHERE studentId = :studentId ORDER BY examTerm ASC, subject ASC")
    fun getGradesForStudent(studentId: Long): Flow<List<GradeEntity>>

    @Query("SELECT * FROM grades ORDER BY id DESC")
    fun getAllGrades(): Flow<List<GradeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrades(grades: List<GradeEntity>)

    @Delete
    suspend fun deleteGrade(grade: GradeEntity)

    // --- PAYMENTS ---
    @Query("SELECT * FROM payments ORDER BY id DESC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE studentId = :studentId ORDER BY id DESC")
    fun getPaymentsForStudent(studentId: Long): Flow<List<PaymentEntity>>

    @Query("SELECT SUM(amount) FROM payments WHERE status = 'PAID'")
    fun getTotalCollectedAmount(): Flow<Double?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayments(payments: List<PaymentEntity>)

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)

    // --- ANNOUNCEMENTS ---
    @Query("SELECT * FROM announcements ORDER BY id DESC")
    fun getAllAnnouncements(): Flow<List<AnnouncementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncement(announcement: AnnouncementEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnnouncements(announcements: List<AnnouncementEntity>)

    @Delete
    suspend fun deleteAnnouncement(announcement: AnnouncementEntity)

    // --- SCHEDULE ---
    @Query("SELECT * FROM schedules WHERE gradeClass = :gradeClass AND dayOfWeek = :dayOfWeek ORDER BY periodNumber ASC")
    fun getScheduleForClassAndDay(gradeClass: String, dayOfWeek: String): Flow<List<ScheduleEntity>>

    @Query("SELECT * FROM schedules WHERE gradeClass = :gradeClass ORDER BY periodNumber ASC")
    fun getScheduleForClass(gradeClass: String): Flow<List<ScheduleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(schedules: List<ScheduleEntity>)
}
