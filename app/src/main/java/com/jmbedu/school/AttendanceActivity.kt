package com.jmbedu.school

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.jmbedu.school.data.local.AttendanceRecord
import com.jmbedu.school.data.local.DemoStudent
import com.jmbedu.school.data.local.JmbEduDatabase
import com.jmbedu.school.data.repository.AttendanceRepository
import com.jmbedu.school.ui.attendance.AttendanceViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

class AttendanceActivity : AppCompatActivity() {
    private lateinit var viewModel: AttendanceViewModel
    private lateinit var attendanceDate: String
    private lateinit var attendanceDateText: TextView
    private lateinit var attendanceSummaryText: TextView
    private lateinit var attendanceListContainer: LinearLayout
    private lateinit var saveAttendanceButton: MaterialButton

    private var currentStudents: List<DemoStudent> = emptyList()
    private var currentRecords: List<AttendanceRecord> = emptyList()
    private val selectedStatuses = mutableMapOf<String, String>()
    private var isStudentPreview = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance)

        attendanceDate = LocalDate.now().toString()
        isStudentPreview = intent.getStringExtra(MainActivity.EXTRA_DEMO_ROLE) == "Student"

        val database = JmbEduDatabase.getInstance(applicationContext)
        viewModel = ViewModelProvider(
            this,
            AttendanceViewModel.Factory(AttendanceRepository(database))
        )[AttendanceViewModel::class.java]

        attendanceDateText = findViewById(R.id.attendanceDateText)
        attendanceSummaryText = findViewById(R.id.attendanceSummaryText)
        attendanceListContainer = findViewById(R.id.attendanceListContainer)
        saveAttendanceButton = findViewById(R.id.saveAttendanceButton)

        attendanceDateText.text = "Date: $attendanceDate"
        if (isStudentPreview) {
            saveAttendanceButton.visibility = View.GONE
            attendanceDateText.text = "My attendance preview • $attendanceDate"
        }
        findViewById<MaterialButton>(R.id.backFromAttendanceButton).setOnClickListener { finish() }
        saveAttendanceButton.setOnClickListener {
            if (currentStudents.isEmpty()) {
                Toast.makeText(this, "Add sample students before taking attendance.", Toast.LENGTH_LONG).show()
            } else {
                viewModel.saveAttendance(
                    attendanceDate, currentStudents, selectedStatuses.toMap(),
                    onSaved = {
                        Toast.makeText(this, "Demo attendance saved on this device.", Toast.LENGTH_SHORT).show()
                    },
                    onError = {
                        Toast.makeText(this, "Could not save demo attendance. Please try again.", Toast.LENGTH_LONG).show()
                    }
                )
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.students.collect { students ->
                        currentStudents = students
                        renderAttendance()
                    }
                }
                launch {
                    viewModel.observeAttendance(attendanceDate).collect { records ->
                        currentRecords = records
                        selectedStatuses.clear()
                        records.forEach { selectedStatuses[it.studentId] = it.status }
                        renderAttendance()
                    }
                }
            }
        }
    }

    private fun renderAttendance() {
        attendanceListContainer.removeAllViews()
        val presentCount = currentStudents.count { selectedStatuses[it.id] == "Present" }
        val absentCount = currentStudents.count { selectedStatuses[it.id] == "Absent" }
        val notMarked = currentStudents.size - presentCount - absentCount
        attendanceSummaryText.text =
            "Present: $presentCount • Absent: $absentCount • Not marked: $notMarked"

        if (currentStudents.isEmpty()) {
            attendanceListContainer.addView(TextView(this).apply {
                text = "No sample students yet. Open Student directory from the dashboard and add a sample record first."
                textSize = 14f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(dp(14), dp(18), dp(14), dp(18))
            })
            return
        }

        currentStudents.forEach { student ->
            val card = MaterialCardView(this).apply {
                radius = dp(16).toFloat()
                cardElevation = dp(1).toFloat()
                setCardBackgroundColor(getColor(android.R.color.white))
                strokeWidth = dp(1)
                strokeColor = getColor(R.color.jmb_background)
                setContentPadding(dp(14), dp(12), dp(14), dp(10))
                layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply { bottomMargin = dp(10) }
            }
            val column = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
            column.addView(TextView(this).apply {
                text = student.name
                textSize = 16f
                setTextColor(getColor(R.color.jmb_text))
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            })
            column.addView(TextView(this).apply {
                text = student.className
                textSize = 13f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(0, dp(2), 0, dp(4))
            })

            val group = RadioGroup(this).apply {
                orientation = RadioGroup.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
            }
            val present = RadioButton(this).apply {
                id = View.generateViewId()
                text = "Present"
                isEnabled = !isStudentPreview
            }
            val absent = RadioButton(this).apply {
                id = View.generateViewId()
                text = "Absent"
                isEnabled = !isStudentPreview
            }
            group.addView(present, RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            group.addView(absent, RadioGroup.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
            when (selectedStatuses[student.id]) {
                "Present" -> group.check(present.id)
                "Absent" -> group.check(absent.id)
            }
            group.setOnCheckedChangeListener { _, checkedId ->
                if (checkedId == present.id) selectedStatuses[student.id] = "Present"
                if (checkedId == absent.id) selectedStatuses[student.id] = "Absent"
                updateSummary()
            }
            column.addView(group)
            card.addView(column)
            attendanceListContainer.addView(card)
        }
        if (isStudentPreview && currentRecords.isEmpty()) {
            attendanceListContainer.addView(TextView(this).apply {
                text = "No demo attendance has been recorded for today."
                textSize = 13f
                setTextColor(getColor(R.color.jmb_muted))
                setPadding(0, dp(8), 0, dp(8))
            })
        }
    }

    private fun updateSummary() {
        val presentCount = currentStudents.count { selectedStatuses[it.id] == "Present" }
        val absentCount = currentStudents.count { selectedStatuses[it.id] == "Absent" }
        val notMarked = currentStudents.size - presentCount - absentCount
        attendanceSummaryText.text =
            "Present: $presentCount • Absent: $absentCount • Not marked: $notMarked"
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}