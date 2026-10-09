package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rollNumber: String,
    val fullName: String,
    val gradeClass: String, // e.g., "Form 4A", "Form 3B", "Fasalka 8aad"
    val parentName: String,
    val parentPhone: String,
    val gender: String, // "M" or "F"
    val dateOfBirth: String,
    val admissionDate: String,
    val photoUri: String? = null,
    val status: String = "ACTIVE" // "ACTIVE", "TRANSFERRED", "GRADUATED"
)
