package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "announcements")
data class AnnouncementEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val category: String, // "IMTIXAAN", "MUHIIM", "FASAX", "GUUD"
    val postedDate: String,
    val author: String = "Maamulka Dugsiga"
)
