package com.example.mylifecycleapp

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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val usernameInput: EditText = findViewById(R.id.usernameInput)
        val passwordInput: EditText = findViewById(R.id.passwordInput)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)

        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            val userPrefs: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedPassword = userPrefs.getString("user_${username}_password", null)

            if (password == savedPassword) {
                // Save login status
                val userEditor: SharedPreferences.Editor = userPrefs.edit()
                userEditor.putBoolean("isLoggedIn", true)
                userEditor.putString("loggedInUser", username)
                userEditor.apply()

                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
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
