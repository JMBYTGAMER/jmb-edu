package com.jmbedu.school.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey val id: String,
    val studentId: String,
    val studentName: String,
    val date: String,
    val status: String
)