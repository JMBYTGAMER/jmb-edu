package com.jmbedu.school.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "demo_students")
data class DemoStudent(
    @PrimaryKey val id: String,
    val name: String,
    val className: String,
    val createdAt: Long = System.currentTimeMillis()
)