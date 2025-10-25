package com.example.loginnavigation


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    private lateinit var welcomeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        welcomeText = findViewById(R.id.welcomeText)

        // ✅ Ensure default text if USERNAME is missing
        val username = intent.getStringExtra("USERNAME") ?: "User"
        welcomeText.text = "Welcome, $username!"
    }
}
