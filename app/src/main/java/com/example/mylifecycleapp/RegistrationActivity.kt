package com.example.mylifecycleapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class RegistrationActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        val usernameInput: EditText = findViewById(R.id.usernameInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val personalNumberInput: EditText = findViewById(R.id.personalNumberInput)
        val registerButton: Button = findViewById(R.id.registerButton)

        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()
            val email = emailInput.text.toString()
            val personalNumber = personalNumberInput.text.toString()


            if (username.isEmpty() || password.isEmpty() || email.isEmpty() || personalNumber.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save registration data with unique key
            with(sharedPreferences.edit()) {
                putString("user_${username}_password", password)
                putString("user_${username}_email", email)
                putString("user_${username}_personalNumber", personalNumber)
                apply()
            }

            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_login -> {
                startActivity(Intent(this, LoginActivity::class.java))
                true
            }
            R.id.action_profile -> {
                startActivity(Intent(this, ProfileActivity::class.java))
                true
            }
            R.id.action_logout -> {
                val userPrefs: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                val userEditor: SharedPreferences.Editor = userPrefs.edit()
                userEditor.putBoolean("isLoggedIn", false)
                userEditor.remove("loggedInUser")
                userEditor.apply()

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
                true
            }
            R.id.action_registration -> {
                startActivity(Intent(this, RegistrationActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
