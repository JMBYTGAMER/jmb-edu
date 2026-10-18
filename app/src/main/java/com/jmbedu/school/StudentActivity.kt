package com.jmbedu.school

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class StudentActivity : AppCompatActivity() {
    private val demoPreferences by lazy {
        getSharedPreferences("jmb_edu_demo_only", MODE_PRIVATE)
    }
    private lateinit var studentNameInput: TextInputEditText
    private lateinit var studentClassInput: TextInputEditText
    private lateinit var studentCountText: TextView
    private lateinit var studentListContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)
        studentNameInput = findViewById(R.id.studentNameInput)
        studentClassInput = findViewById(R.id.studentClassInput)
        studentCountText = findViewById(R.id.studentCountText)
        studentListContainer = findViewById(R.id.studentListContainer)

        findViewById<MaterialButton>(R.id.backToDashboardButton).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.addStudentButton).setOnClickListener { addSampleStudent() }
        renderStudents()
    }

    private fun addSampleStudent() {
        val name = studentNameInput.text?.toString()?.trim().orEmpty()
        val className = studentClassInput.text?.toString()?.trim().orEmpty()
        if (name.isBlank()) {
            studentNameInput.error = "Enter a sample name"
            return
        }
        if (className.isBlank()) {
            studentClassInput.error = "Enter a sample class"
            return
        }
        val students = readStudents()
        students.put(JSONObject().put("id", UUID.randomUUID().toString())
            .put("name", name).put("className", className))
        demoPreferences.edit().putString(PREF_STUDENTS, students.toString()).apply()
        studentNameInput.text?.clear()
        studentClassInput.text?.clear()
        renderStudents()
        Toast.makeText(this, "Sample student saved on this device.", Toast.LENGTH_SHORT).show()
    }

    private fun deleteStudent(studentId: String) {
        val current = readStudents()
        val kept = JSONArray()
        for (index in 0 until current.length()) {
            val student = current.optJSONObject(index) ?: continue
            if (student.optString("id") != studentId) kept.put(student)
        }
        demoPreferences.edit().putString(PREF_STUDENTS, kept.toString()).apply()
        renderStudents()
    }

    private fun readStudents(): JSONArray = try {
        JSONArray(demoPreferences.getString(PREF_STUDENTS, "[]") ?: "[]")
    } catch (_: Exception) {
        JSONArray()
    }

    private fun renderStudents() {
        val students = readStudents()
        studentListContainer.removeAllViews()
        studentCountText.text = "Sample students (\u0024{students.length()})"
        if (students.length() == 0) {
            studentListContainer.addView(TextView(this).apply {
                text = "No sample students yet. Add one above to try the directory."
                textSize = 14f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(dp(14), dp(18), dp(14), dp(18))
            })
            return
        }
        for (index in 0 until students.length()) {
            val student = students.optJSONObject(index) ?: continue
            val id = student.optString("id")
            val name = student.optString("name", "Sample student")
            val className = student.optString("className", "Class not set")
            val card = MaterialCardView(this).apply {
                radius = dp(16).toFloat()
                cardElevation = dp(1).toFloat()
                setCardBackgroundColor(getColor(android.R.color.white))
                strokeWidth = dp(1)
                strokeColor = getColor(R.color.jmb_background)
                setContentPadding(dp(14), dp(10), dp(10), dp(10))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dp(10) }
            }
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            val details = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            details.addView(TextView(this).apply {
                text = name
                textSize = 16f
                setTextColor(getColor(R.color.jmb_text))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            })
            details.addView(TextView(this).apply {
                text = className
                textSize = 13f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(0, dp(3), 0, 0)
            })
            val deleteButton = MaterialButton(this).apply {
                text = "Delete"
                isAllCaps = false
                contentDescription = "Delete sample student $name"
                setOnClickListener { deleteStudent(id) }
            }
            row.addView(details)
            row.addView(deleteButton)
            card.addView(row)
            studentListContainer.addView(card)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

    companion object {
        private const val PREF_STUDENTS = "sample_students_json"
    }
}