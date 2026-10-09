package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedules")
data class ScheduleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dayOfWeek: String, // "Sabti", "Axad", "Isniin", "Talaado", "Arbaco", "Khamiis"
    val periodNumber: Int, // 1 to 6
    val timeSlot: String, // "07:30 - 08:15", etc.
    val gradeClass: String, // "Form 4A", "Form 3A", etc.
    val subject: String,
    val teacherName: String,
    val roomNumber: String
)
