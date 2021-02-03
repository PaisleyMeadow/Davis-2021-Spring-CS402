package com.paisleydavis.transcribe

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //set username
        val usernameStr = intent.getStringExtra("username")
        val usernameText = findViewById<TextView>(R.id.username)
        usernameText.text = "$usernameStr."

        //arrays for day and month names
        val dayNames = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        //get current date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = monthNames[c.get(Calendar.MONTH)]
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dayName = dayNames[c.get(Calendar.DAY_OF_WEEK)]

        //set date
        val dateText = findViewById<TextView>(R.id.profileDate)
        val dateStr = "$dayName, $month $day, $year."
        dateText.text = dateStr
    }
}