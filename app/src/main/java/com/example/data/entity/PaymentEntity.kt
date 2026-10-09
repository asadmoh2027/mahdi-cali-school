package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentId: Long,
    val studentName: String,
    val rollNumber: String,
    val gradeClass: String,
    val receiptNumber: String,
    val amount: Double, // e.g., 25.0 USD
    val monthCovered: String, // e.g., "Janaayo 2025", "Febraayo 2025"
    val paymentDate: String,
    val paymentMethod: String, // "EVC Plus", "Zaad Service", "Sahal", "Kaash / Cash"
    val status: String = "PAID", // "PAID", "PENDING"
    val note: String = ""
)
