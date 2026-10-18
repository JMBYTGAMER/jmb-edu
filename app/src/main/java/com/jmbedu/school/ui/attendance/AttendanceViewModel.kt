package com.jmbedu.school.ui.attendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.repository.AttendanceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AttendanceViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {
    val students: Flow<List<DemoStudent>> = repository.observeStudents()

    fun observeAttendance(date: String) = repository.observeAttendance(date)

    fun saveAttendance(
        date: String,
        students: List<DemoStudent>,
        statuses: Map<String, String>,
        onSaved: () -> Unit,
        onError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.saveAttendance(date, students, statuses)
                onSaved()
            } catch (_: Exception) {
                onError()
            }
        }
    }

    class Factory(
        private val repository: AttendanceRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AttendanceViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}