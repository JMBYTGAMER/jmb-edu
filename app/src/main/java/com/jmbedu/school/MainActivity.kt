package com.jmbedu.school

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private val demoRole: String
        get() = intent.getStringExtra(EXTRA_DEMO_ROLE) ?: "Student"
    private val demoEmail: String
        get() = intent.getStringExtra(EXTRA_DEMO_EMAIL).orEmpty()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val displayName = demoEmail.substringBefore("@").ifBlank { "Demo User" }
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        findViewById<TextView>(R.id.dashboardWelcome).text = "Welcome, $displayName"
        findViewById<TextView>(R.id.dashboardRole).text = "$demoRole preview • DEMO MODE"

        val studentsButton = findViewById<MaterialButton>(R.id.studentsButton)
        studentsButton.text = if (demoRole.equals("Student", true)) {
            "My student preview"
        } else {
            "Manage demo students"
        }
        studentsButton.setOnClickListener {
            if (demoRole.equals("Student", true)) {
                AlertDialog.Builder(this)
                    .setTitle("Student preview")
                    .setMessage("This is a sample student workspace. Real student profiles will appear after secure school accounts are connected.")
                    .setPositiveButton("Got it", null)
                    .show()
            } else {
                startActivity(Intent(this, StudentActivity::class.java)
                    .putExtra(EXTRA_DEMO_ROLE, demoRole))
            }
        }
        bindComingSoon(R.id.teachersButton, "Teacher directory")
        findViewById<MaterialButton>(R.id.attendanceButton).setOnClickListener {
            startActivity(Intent(this, AttendanceActivity::class.java)
                .putExtra(EXTRA_DEMO_ROLE, demoRole))
        }
        bindComingSoon(R.id.reportsButton, "Exams and reports")
        findViewById<MaterialButton>(R.id.signOutButton).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun bindComingSoon(buttonId: Int, moduleName: String) {
        findViewById<MaterialButton>(buttonId).setOnClickListener {
            Toast.makeText(this,
                "$moduleName is next on the demo roadmap. No server or real school account is connected.",
                Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        const val EXTRA_DEMO_ROLE = "com.jmbedu.school.DEMO_ROLE"
        const val EXTRA_DEMO_EMAIL = "com.jmbedu.school.DEMO_EMAIL"
    }
}