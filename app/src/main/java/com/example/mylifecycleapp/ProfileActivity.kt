package com.example.mylifecycleapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private var loggedInUser: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)

        loggedInUser = sharedPreferences.getString("loggedInUser", null)

        if (loggedInUser == null) {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        val ageInput: EditText = findViewById(R.id.ageInput)
        val hasLicenseCheckbox: CheckBox = findViewById(R.id.hasLicenseCheckbox)
        val genderRadioGroup: RadioGroup = findViewById(R.id.genderRadioGroup)
        val emailInput: EditText = findViewById(R.id.emailInput)
        val submitButton: Button = findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val age = ageInput.text.toString()
            val hasLicense = hasLicenseCheckbox.isChecked
            val gender = when (genderRadioGroup.checkedRadioButtonId) {
                R.id.maleRadioButton -> "Male"
                R.id.femaleRadioButton -> "Female"
                else -> "Other"
            }
            val email = emailInput.text.toString()

            // Save data with SharedPreferences
            with(sharedPreferences.edit()) {
                putString("user_${loggedInUser}_age", age)
                putBoolean("user_${loggedInUser}_hasLicense", hasLicense)
                putString("user_${loggedInUser}_gender", gender)
                putString("user_${loggedInUser}_email", email)
                apply()
            }

            Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
        }

        loadProfileData()
    }

    private fun loadProfileData() {
        val age = sharedPreferences.getString("user_${loggedInUser}_age", "")
        val hasLicense = sharedPreferences.getBoolean("user_${loggedInUser}_hasLicense", false)
        val gender = sharedPreferences.getString("user_${loggedInUser}_gender", "")
        val email = sharedPreferences.getString("user_${loggedInUser}_email", "")

        findViewById<EditText>(R.id.ageInput).setText(age)
        findViewById<CheckBox>(R.id.hasLicenseCheckbox).isChecked = hasLicense
        findViewById<EditText>(R.id.emailInput).setText(email)

        val genderRadioGroup: RadioGroup = findViewById(R.id.genderRadioGroup)
        when (gender) {
            "Male" -> genderRadioGroup.check(R.id.maleRadioButton)
            "Female" -> genderRadioGroup.check(R.id.femaleRadioButton)
            "Other" -> genderRadioGroup.check(R.id.otherRadioButton)
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
