package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.SchoolDao
import com.example.data.entity.AnnouncementEntity
import com.example.data.entity.AttendanceEntity
import com.example.data.entity.GradeEntity
import com.example.data.entity.PaymentEntity
import com.example.data.entity.ScheduleEntity
import com.example.data.entity.StudentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Database(
    entities = [
        StudentEntity::class,
        AttendanceEntity::class,
        GradeEntity::class,
        PaymentEntity::class,
        AnnouncementEntity::class,
        ScheduleEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SchoolDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao

    companion object {
        @Volatile
        private var INSTANCE: SchoolDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): SchoolDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SchoolDatabase::class.java,
                    "mahdi_cali_school_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(SchoolDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class SchoolDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialData(database.schoolDao())
                }
            }
        }

        private suspend fun populateInitialData(dao: SchoolDao) {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

            val students = listOf(
                StudentEntity(
                    id = 1,
                    rollNumber = "MCS-2024-001",
                    fullName = "Cabdiraxmaan Mahdi Cali",
                    gradeClass = "Form 4A",
                    parentName = "Mahdi Cali Nuur",
                    parentPhone = "+252 61 555 1234",
                    gender = "M",
                    dateOfBirth = "2007-04-12",
                    admissionDate = "2021-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 2,
                    rollNumber = "MCS-2024-002",
                    fullName = "Hodan Axmed Xasan",
                    gradeClass = "Form 4A",
                    parentName = "Axmed Xasan Geedi",
                    parentPhone = "+252 61 789 4521",
                    gender = "F",
                    dateOfBirth = "2007-08-20",
                    admissionDate = "2021-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 3,
                    rollNumber = "MCS-2024-003",
                    fullName = "Khaalid Cabdullaahi Yuusuf",
                    gradeClass = "Form 4A",
                    parentName = "Cabdullaahi Yuusuf Cilmi",
                    parentPhone = "+252 61 333 9988",
                    gender = "M",
                    dateOfBirth = "2006-11-15",
                    admissionDate = "2021-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 4,
                    rollNumber = "MCS-2024-004",
                    fullName = "Farxiyo Maxamed Warsame",
                    gradeClass = "Form 4A",
                    parentName = "Maxamed Warsame Jaamac",
                    parentPhone = "+252 61 222 7744",
                    gender = "F",
                    dateOfBirth = "2007-02-18",
                    admissionDate = "2021-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 5,
                    rollNumber = "MCS-2024-015",
                    fullName = "Mustafe Cilmi Guuleed",
                    gradeClass = "Form 3A",
                    parentName = "Cilmi Guuleed Barre",
                    parentPhone = "+252 61 888 1122",
                    gender = "M",
                    dateOfBirth = "2008-05-10",
                    admissionDate = "2022-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 6,
                    rollNumber = "MCS-2024-016",
                    fullName = "Sahra Cali Cumar",
                    gradeClass = "Form 3A",
                    parentName = "Cali Cumar Rooble",
                    parentPhone = "+252 61 999 4433",
                    gender = "F",
                    dateOfBirth = "2008-09-25",
                    admissionDate = "2022-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 7,
                    rollNumber = "MCS-2024-025",
                    fullName = "Yuusuf Xuseen Maxamuud",
                    gradeClass = "Form 2B",
                    parentName = "Xuseen Maxamuud Shire",
                    parentPhone = "+252 61 444 6655",
                    gender = "M",
                    dateOfBirth = "2009-03-30",
                    admissionDate = "2023-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 8,
                    rollNumber = "MCS-2024-026",
                    fullName = "Nimco Ismaaciil Aadan",
                    gradeClass = "Form 2B",
                    parentName = "Ismaaciil Aadan Warsame",
                    parentPhone = "+252 61 777 3322",
                    gender = "F",
                    dateOfBirth = "2009-07-14",
                    admissionDate = "2023-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 9,
                    rollNumber = "MCS-2024-040",
                    fullName = "Maxamed Bashiir Nuur",
                    gradeClass = "Fasalka 8aad",
                    parentName = "Bashiir Nuur Cilmi",
                    parentPhone = "+252 61 111 8899",
                    gender = "M",
                    dateOfBirth = "2010-10-05",
                    admissionDate = "2020-09-01",
                    status = "ACTIVE"
                ),
                StudentEntity(
                    id = 10,
                    rollNumber = "MCS-2024-041",
                    fullName = "Fadumo Cabdi Sharmaarke",
                    gradeClass = "Fasalka 8aad",
                    parentName = "Cabdi Sharmaarke Cali",
                    parentPhone = "+252 61 666 2211",
                    gender = "F",
                    dateOfBirth = "2010-12-12",
                    admissionDate = "2020-09-01",
                    status = "ACTIVE"
                )
            )
            dao.insertStudents(students)

            // Initial Attendance for Today
            val attendanceRecords = listOf(
                AttendanceEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 2, studentName = "Hodan Axmed Xasan", gradeClass = "Form 4A", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 3, studentName = "Khaalid Cabdullaahi Yuusuf", gradeClass = "Form 4A", date = today, status = "LATE", notes = "Gaadiidka ayaa ku daahay"),
                AttendanceEntity(studentId = 4, studentName = "Farxiyo Maxamed Warsame", gradeClass = "Form 4A", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 5, studentName = "Mustafe Cilmi Guuleed", gradeClass = "Form 3A", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 6, studentName = "Sahra Cali Cumar", gradeClass = "Form 3A", date = today, status = "EXCUSED", notes = "Xanuun yar oo fudud"),
                AttendanceEntity(studentId = 7, studentName = "Yuusuf Xuseen Maxamuud", gradeClass = "Form 2B", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 8, studentName = "Nimco Ismaaciil Aadan", gradeClass = "Form 2B", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 9, studentName = "Maxamed Bashiir Nuur", gradeClass = "Fasalka 8aad", date = today, status = "PRESENT"),
                AttendanceEntity(studentId = 10, studentName = "Fadumo Cabdi Sharmaarke", gradeClass = "Fasalka 8aad", date = today, status = "PRESENT")
            )
            dao.recordAttendanceBatch(attendanceRecords)

            // Initial Grades
            val grades = listOf(
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Xisaab (Math)", score = 96.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Fiisikis (Physics)", score = 92.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Kimistari (Chemistry)", score = 89.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "English", score = 94.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Tarbiyo Islaam", score = 98.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Af-Soomaali", score = 95.0, recordedDate = "2024-11-20"),

                GradeEntity(studentId = 2, studentName = "Hodan Axmed Xasan", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Xisaab (Math)", score = 94.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 2, studentName = "Hodan Axmed Xasan", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Bayooloji (Biology)", score = 97.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 2, studentName = "Hodan Axmed Xasan", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "English", score = 91.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 2, studentName = "Hodan Axmed Xasan", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Tarbiyo Islaam", score = 99.0, recordedDate = "2024-11-20"),

                GradeEntity(studentId = 3, studentName = "Khaalid Cabdullaahi Yuusuf", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Xisaab (Math)", score = 85.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 3, studentName = "Khaalid Cabdullaahi Yuusuf", gradeClass = "Form 4A", examTerm = "Teeramka 1aad", subject = "Fiisikis (Physics)", score = 88.0, recordedDate = "2024-11-20"),

                GradeEntity(studentId = 5, studentName = "Mustafe Cilmi Guuleed", gradeClass = "Form 3A", examTerm = "Teeramka 1aad", subject = "Xisaab (Math)", score = 91.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 5, studentName = "Mustafe Cilmi Guuleed", gradeClass = "Form 3A", examTerm = "Teeramka 1aad", subject = "Saynis (Science)", score = 89.0, recordedDate = "2024-11-20"),

                GradeEntity(studentId = 9, studentName = "Maxamed Bashiir Nuur", gradeClass = "Fasalka 8aad", examTerm = "Teeramka 1aad", subject = "Xisaab (Math)", score = 95.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 9, studentName = "Maxamed Bashiir Nuur", gradeClass = "Fasalka 8aad", examTerm = "Teeramka 1aad", subject = "Saynis (Science)", score = 93.0, recordedDate = "2024-11-20"),
                GradeEntity(studentId = 9, studentName = "Maxamed Bashiir Nuur", gradeClass = "Fasalka 8aad", examTerm = "Teeramka 1aad", subject = "Af-Soomaali", score = 96.0, recordedDate = "2024-11-20")
            )
            dao.insertGrades(grades)

            // Initial Payments
            val payments = listOf(
                PaymentEntity(studentId = 1, studentName = "Cabdiraxmaan Mahdi Cali", rollNumber = "MCS-2024-001", gradeClass = "Form 4A", receiptNumber = "REC-10492", amount = 30.0, monthCovered = "Bisha Janaayo 2025", paymentDate = "2025-01-05", paymentMethod = "EVC Plus", status = "PAID"),
                PaymentEntity(studentId = 2, studentName = "Hodan Axmed Xasan", rollNumber = "MCS-2024-002", gradeClass = "Form 4A", receiptNumber = "REC-10493", amount = 30.0, monthCovered = "Bisha Janaayo 2025", paymentDate = "2025-01-06", paymentMethod = "Zaad Service", status = "PAID"),
                PaymentEntity(studentId = 3, studentName = "Khaalid Cabdullaahi Yuusuf", rollNumber = "MCS-2024-003", gradeClass = "Form 4A", receiptNumber = "REC-10494", amount = 30.0, monthCovered = "Bisha Janaayo 2025", paymentDate = "2025-01-07", paymentMethod = "Sahal", status = "PAID"),
                PaymentEntity(studentId = 5, studentName = "Mustafe Cilmi Guuleed", rollNumber = "MCS-2024-015", gradeClass = "Form 3A", receiptNumber = "REC-10512", amount = 25.0, monthCovered = "Bisha Janaayo 2025", paymentDate = "2025-01-04", paymentMethod = "EVC Plus", status = "PAID"),
                PaymentEntity(studentId = 9, studentName = "Maxamed Bashiir Nuur", rollNumber = "MCS-2024-040", gradeClass = "Fasalka 8aad", receiptNumber = "REC-10530", amount = 20.0, monthCovered = "Bisha Janaayo 2025", paymentDate = "2025-01-08", paymentMethod = "Kaash / Cash", status = "PAID")
            )
            dao.insertPayments(payments)

            // Initial Announcements
            val announcements = listOf(
                AnnouncementEntity(
                    title = "Furitaanka Teeramka Cusub iyo Diiwaangalinta",
                    content = "Maamulka Dugsiga Mahdi Cali wuxuu ku wargelinayaa dhammaan ardayda iyo waalidiinta in waxbarashada teeramka cusub ay si toos ah u bilaaban doonto Sabtida soo socota. Dhammaan ardayda waxaa laga codsanayaa inay ilaaliyaan waqtiga lebbiskana dhammaystiraan.",
                    category = "MUHIIM",
                    postedDate = "2025-01-02",
                    author = "Ustaad Mahdi Cali (Maamulaha)"
                ),
                AnnouncementEntity(
                    title = "Jadwalka Imtixaanaadka Teeramka Dhexe",
                    content = "Imtixaanka teeramka dhexe (Midterm Exams) wuxuu bilaaban doonaa 15-ka bishan. Fadlan ardaydu ha u diyaar garoobaan waxbarashada iyo casharrada lagu qaatay fasalka.",
                    category = "IMTIXAAN",
                    postedDate = "2025-01-10",
                    author = "Xafiiska Imtixaanaadka"
                ),
                AnnouncementEntity(
                    title = "Shirka Guud ee Waalidiinta iyo Macallimiinta",
                    content = "Waxaa jira kulan ballaaran oo dhexmari doona maamulka dugsiga, macallimiinta, iyo waalidiinta sharafta leh maalinta Khamiista ah si looga wada hadlo horumarka waxbarasho ee ardayda.",
                    category = "GUUD",
                    postedDate = "2025-01-14",
                    author = "Guddiga Waalidiinta & Dugsiga"
                ),
                AnnouncementEntity(
                    title = "Tartanka Aqoonta iyo Qur'aanka Kariimka",
                    content = "Dugsiga Mahdi Cali wuxuu qabanayaa tartankii sanadlaha ahaa ee xifdinta Qur'aanka iyo aqoonta guud. Ardayda doonaysa inay ka qaybgalaan ha iska diiwaangeliyaan xafiiska tarbiyada.",
                    category = "FASAX",
                    postedDate = "2025-01-18",
                    author = "Qaybta Tarbiyada & Dhaqanka"
                )
            )
            dao.insertAnnouncements(announcements)

            // Initial Schedules
            val schedules = listOf(
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 1, timeSlot = "07:30 - 08:15", gradeClass = "Form 4A", subject = "Xisaab (Mathematics)", teacherName = "Ustaad Mahdi Cali", roomNumber = "Class 401"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 2, timeSlot = "08:15 - 09:00", gradeClass = "Form 4A", subject = "Fiisikis (Physics)", teacherName = "Ustaad Axmed", roomNumber = "Lab 1"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 3, timeSlot = "09:15 - 10:00", gradeClass = "Form 4A", subject = "English Language", teacherName = "Macallim Maryan", roomNumber = "Class 401"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 4, timeSlot = "10:00 - 10:45", gradeClass = "Form 4A", subject = "Tarbiyada Islaamka", teacherName = "Sheekh Cabdi", roomNumber = "Class 401"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 5, timeSlot = "11:00 - 11:45", gradeClass = "Form 4A", subject = "Kimistari (Chemistry)", teacherName = "Ustaad Xuseen", roomNumber = "Lab 2"),

                ScheduleEntity(dayOfWeek = "Axad", periodNumber = 1, timeSlot = "07:30 - 08:15", gradeClass = "Form 4A", subject = "Bayooloji (Biology)", teacherName = "Ustaad Faarax", roomNumber = "Lab 1"),
                ScheduleEntity(dayOfWeek = "Axad", periodNumber = 2, timeSlot = "08:15 - 09:00", gradeClass = "Form 4A", subject = "Af-Soomaali", teacherName = "Macallim Khadiijo", roomNumber = "Class 401"),
                ScheduleEntity(dayOfWeek = "Axad", periodNumber = 3, timeSlot = "09:15 - 10:00", gradeClass = "Form 4A", subject = "Carabi (Arabic)", teacherName = "Ustaad Cumar", roomNumber = "Class 401"),
                ScheduleEntity(dayOfWeek = "Axad", periodNumber = 4, timeSlot = "10:00 - 10:45", gradeClass = "Form 4A", subject = "Xisaab (Mathematics)", teacherName = "Ustaad Mahdi Cali", roomNumber = "Class 401"),

                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 1, timeSlot = "07:30 - 08:15", gradeClass = "Form 3A", subject = "Af-Soomaali", teacherName = "Macallim Khadiijo", roomNumber = "Class 302"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 2, timeSlot = "08:15 - 09:00", gradeClass = "Form 3A", subject = "Xisaab (Mathematics)", teacherName = "Ustaad Mahdi Cali", roomNumber = "Class 302"),
                ScheduleEntity(dayOfWeek = "Sabti", periodNumber = 3, timeSlot = "09:15 - 10:00", gradeClass = "Form 3A", subject = "Saynis (Science)", teacherName = "Ustaad Xasan", roomNumber = "Lab 1")
            )
            dao.insertSchedules(schedules)
        }
    }
}
