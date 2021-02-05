package com.paisleydavis.transcribe

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*


class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //set username
        val usernameStr = intent.getStringExtra("username")
        val usernameText = findViewById<TextView>(R.id.username)
        if (usernameStr != null) {
            usernameText.text = "$usernameStr."
        }
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

        //hide med edit layout
        editLayout.visibility = View.GONE

        //hide mood edit layout
        moodLayout.visibility = View.GONE

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val medReminderSwitch = findViewById<Switch>(R.id.med1Switch)

        //delete medication
        med1Delete.setOnClickListener {
            med1Container.visibility = View.GONE

            editLayout.visibility = View.GONE
        }

        //edit medication
        med1Add.setOnClickListener {
            //fill name and dosage
            val name = med1Name.text.toString()
            editMedName.setText(name)

            val dose = med1Dose.text.toString()
            editDosage.setText(dose)

            if (editLayout.visibility == View.VISIBLE) {
                editLayout.visibility = View.GONE
            } else {
                editLayout.visibility = View.VISIBLE
            }
        }

        editSubmit.setOnClickListener {
            editLayout.visibility = View.GONE

            val newName = editMedName.text.toString()
            val newDose = editDosage.text.toString()

            med1Name.text = newName
            med1Dose.text = newDose
        }

        //edit mood
        addMoodBtn.setOnClickListener {
            if(moodLayout.visibility == View.GONE){
                moodLayout.visibility = View.VISIBLE
            }
            else{
                moodLayout.visibility = View.GONE
            }
        }

        //test notification for reminder switch
        medReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                //change med color
                
            } else {
                //do nothing
            }
        }
    }

    //sets saved mood
    fun setMood(view: View) {
//        val drawble = resources.getDrawable(R.drawable.ic_training,theme)

//        val chosenMood = resources.getDrawable(R.drawable.tired, theme)
//                savedMood.setImageDrawable()
    }
}

