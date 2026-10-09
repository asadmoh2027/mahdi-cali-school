package com.example.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "attendance",
    indices = [Index(value = ["studentId", "date"], unique = true)]
)
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val gradeClass: String,
    val date: String, // "YYYY-MM-DD"
    val status: String, // "PRESENT", "ABSENT", "LATE", "EXCUSED"
    val notes: String = ""
)
