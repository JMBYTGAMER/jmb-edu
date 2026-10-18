package com.jmbedu.school.data.repository

import androidx.room.withTransaction
import com.jmbedu.school.data.local.AttendanceRecord
import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.local.JmbEduDatabase
import kotlinx.coroutines.flow.Flow

class AttendanceRepository(private val database: JmbEduDatabase) {
    fun observeStudents(): Flow<List<DemoStudent>> =
        database.demoStudentDao().observeAll()

    fun observeAttendance(date: String): Flow<List<AttendanceRecord>> =
        database.attendanceDao().observeForDate(date)

    suspend fun saveAttendance(
        date: String,
        students: List<DemoStudent>,
        selectedStatuses: Map<String, String>
    ) {
        val records = students.mapNotNull { student ->
            val status = selectedStatuses[student.id]
            if (status != "Present" && status != "Absent") {
                null
            } else {
                AttendanceRecord(
                    id = "$date:${student.id}",
                    studentId = student.id,
                    studentName = student.name,
                    date = date,
                    status = status
                )
            }
        }

        database.withTransaction {
            database.attendanceDao().deleteForDate(date)
            if (records.isNotEmpty()) database.attendanceDao().insertAll(records)
        }
    }
}