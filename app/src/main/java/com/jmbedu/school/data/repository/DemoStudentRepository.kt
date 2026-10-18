package com.jmbedu.school.data.repository

import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.local.DemoStudentDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class DemoStudentRepository(private val studentDao: DemoStudentDao) {
    fun observeStudents(): Flow<List<DemoStudent>> = studentDao.observeAll()

    suspend fun addStudent(name: String, className: String) {
        studentDao.insert(
            DemoStudent(
                id = UUID.randomUUID().toString(),
                name = name.trim(),
                className = className.trim()
            )
        )
    }

    suspend fun deleteStudent(id: String) {
        studentDao.deleteById(id)
    }
}