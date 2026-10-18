package com.jmbedu.school.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AttendanceDao {
    @Query("SELECT * FROM attendance_records WHERE date = :date ORDER BY studentName COLLATE NOCASE")
    fun observeForDate(date: String): Flow<List<AttendanceRecord>>

    @Query("DELETE FROM attendance_records WHERE date = :date")
    suspend fun deleteForDate(date: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<AttendanceRecord>)
}