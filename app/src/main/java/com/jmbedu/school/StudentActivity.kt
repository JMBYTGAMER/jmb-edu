package com.jmbedu.school

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.local.JmbEduDatabase
import com.jmbedu.school.data.repository.DemoStudentRepository
import com.jmbedu.school.ui.students.DemoStudentsViewModel
import kotlinx.coroutines.launch

class StudentActivity : AppCompatActivity() {
    private lateinit var viewModel: DemoStudentsViewModel
    private lateinit var studentNameInput: TextInputEditText
    private lateinit var studentClassInput: TextInputEditText
    private lateinit var studentCountText: TextView
    private lateinit var studentListContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_students)

        val dao = JmbEduDatabase.getInstance(applicationContext).demoStudentDao()
        val repository = DemoStudentRepository(dao)
        viewModel = ViewModelProvider(
            this,
            DemoStudentsViewModel.Factory(repository)
        )[DemoStudentsViewModel::class.java]

        studentNameInput = findViewById(R.id.studentNameInput)
        studentClassInput = findViewById(R.id.studentClassInput)
        studentCountText = findViewById(R.id.studentCountText)
        studentListContainer = findViewById(R.id.studentListContainer)

        findViewById<MaterialButton>(R.id.backToDashboardButton).setOnClickListener { finish() }
        findViewById<MaterialButton>(R.id.addStudentButton).setOnClickListener { addSampleStudent() }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.students.collect { renderStudents(it) }
            }
        }
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
        viewModel.addStudent(name, className)
        studentNameInput.text?.clear()
        studentClassInput.text?.clear()
        Toast.makeText(this, "Sample student added to this device's demo database.", Toast.LENGTH_SHORT).show()
    }

    private fun renderStudents(students: List<DemoStudent>) {
        studentListContainer.removeAllViews()
        studentCountText.text = "Sample students (" + students.size + ")"
        if (students.isEmpty()) {
            studentListContainer.addView(TextView(this).apply {
                text = "No sample students yet. Add one above to try the directory."
                textSize = 14f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(dp(14), dp(18), dp(14), dp(18))
            })
            return
        }

        students.forEach { student ->
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
                text = student.name
                textSize = 16f
                setTextColor(getColor(R.color.jmb_text))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            })
            details.addView(TextView(this).apply {
                text = student.className
                textSize = 13f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(0, dp(3), 0, 0)
            })
            val deleteButton = MaterialButton(this).apply {
                text = "Delete"
                isAllCaps = false
                contentDescription = "Delete sample student ${student.name}"
                setOnClickListener { viewModel.deleteStudent(student.id) }
            }
            row.addView(details)
            row.addView(deleteButton)
            card.addView(row)
            studentListContainer.addView(card)
        }
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}