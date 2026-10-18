package com.jmbedu.school.ui.students

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.repository.DemoStudentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DemoStudentsViewModel(
    private val repository: DemoStudentRepository
) : ViewModel() {
    val students: Flow<List<DemoStudent>> = repository.observeStudents()

    fun addStudent(name: String, className: String) {
        viewModelScope.launch {
            repository.addStudent(name, className)
        }
    }

    fun deleteStudent(id: String) {
        viewModelScope.launch {
            repository.deleteStudent(id)
        }
    }

    class Factory(
        private val repository: DemoStudentRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DemoStudentsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DemoStudentsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}