package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "grades")
data class GradeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val gradeClass: String,
    val examTerm: String, // e.g., "Teeramka 1aad", "Midterm", "Imtixaanka Kama Dambaysta ah"
    val subject: String, // e.g., "Xisaab (Math)", "Saynis (Science)", "Af-Soomaali", "English", "Tarbiyo", "Carabi"
    val score: Double, // 0 - 100
    val maxScore: Double = 100.0,
    val recordedDate: String
)
