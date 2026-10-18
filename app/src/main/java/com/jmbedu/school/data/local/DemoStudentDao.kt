package com.jmbedu.school.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DemoStudentDao {
    @Query("SELECT * FROM demo_students ORDER BY name COLLATE NOCASE")
    fun observeAll(): Flow<List<DemoStudent>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(student: DemoStudent)

    @Query("DELETE FROM demo_students WHERE id = :studentId")
    suspend fun deleteById(studentId: String)
}