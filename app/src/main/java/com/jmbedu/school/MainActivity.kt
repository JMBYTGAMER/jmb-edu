package com.jmbedu.school

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindModule(R.id.studentsButton, "Student management")
        bindModule(R.id.teachersButton, "Teacher management")
        bindModule(R.id.attendanceButton, "Attendance")
        bindModule(R.id.reportsButton, "Exams and reports")
    }

    private fun bindModule(buttonId: Int, moduleName: String) {
        findViewById<MaterialButton>(buttonId).setOnClickListener {
            Toast.makeText(
                this,
                "$moduleName is planned for the next development stage.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
