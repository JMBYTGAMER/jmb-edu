package com.jmbedu.school

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val roles = findViewById<Spinner>(R.id.roleSpinner)
        roles.adapter = ArrayAdapter.createFromResource(
            this, R.array.login_roles, android.R.layout.simple_spinner_item
        ).also { it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item) }

        findViewById<MaterialButton>(R.id.signInButton).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailInput).text?.toString()?.trim().orEmpty()
            val password = findViewById<TextInputEditText>(R.id.passwordInput).text?.toString().orEmpty()
            when {
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                    findViewById<TextInputEditText>(R.id.emailInput).error = "Enter a valid email address"
                password.isBlank() ->
                    findViewById<TextInputEditText>(R.id.passwordInput).error = "Enter your password"
                else -> Toast.makeText(
                    this,
                    "Real account sign-in is not connected yet. Your password was not sent or saved. Use Explore Demo to preview the app.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        findViewById<MaterialButton>(R.id.previewDemoButton).setOnClickListener {
            val email = findViewById<TextInputEditText>(R.id.emailInput).text?.toString()?.trim().orEmpty()
            val selectedRole = roles.selectedItem?.toString() ?: "Student"
            startActivity(Intent(this, MainActivity::class.java)
                .putExtra(MainActivity.EXTRA_DEMO_ROLE, selectedRole)
                .putExtra(MainActivity.EXTRA_DEMO_EMAIL, email))
        }
    }
}