package com.paisleydavis.transcribe

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*


class Profile : AppCompatActivity() {

    //get medication container fragment
    private val medContainerFragment = MedContainerFragment.newInstance("", "")

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


        //place med container fragment into the profile container
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.medFragmentContainer, medContainerFragment)
                .commit()
        }

        /////

//        //hide med edit layout
//        editLayout.visibility = View.GONE
//
//        //hide mood edit layout
//        moodLayout.visibility = View.GONE
//
//        //hide weight edit layout
//        editWeightLayout.visibility = View.GONE

//        @SuppressLint("UseSwitchCompatOrMaterialCode")
//        val medReminderSwitch = findViewById<Switch>(R.id.med1Switch)
//
//        //delete medication
//        //TODO: implement user confirmation for deleting medication
//        med1Delete.setOnClickListener {
//            med1Container.visibility = View.GONE
//
//            editLayout.visibility = View.GONE
//        }
//
//        //edit medication
//        med1Add.setOnClickListener {
//            //fill name and dosage
//            val name = med1Name.text.toString()
//            editMedName.setText(name)
//
//            val dose = med1Dose.text.toString()
//            editDosage.setText(dose)
//
//            if (editLayout.visibility == View.VISIBLE) {
//                editLayout.visibility = View.GONE
//            } else {
//                editLayout.visibility = View.VISIBLE
//            }
//        }

//        editSubmit.setOnClickListener {
//            editLayout.visibility = View.GONE
//
//            val newName = editMedName.text.toString()
//            val newDose = editDosage.text.toString()
//
//            med1Name.text = newName
//            med1Dose.text = newDose
//        }
//
//        //edit mood
//        addMoodBtn.setOnClickListener {
//            if(moodLayout.visibility == View.GONE){
//                editWeightLayout.visibility = View.GONE
//                moodLayout.visibility = View.VISIBLE
//            }
//            else{
//                moodLayout.visibility = View.GONE
//            }
//        }
//
//        //test notification for reminder switch
//        //TODO: set-up test notification when turning on reminder switch
//        medReminderSwitch.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                //change med color
//                med1Container.setBackgroundColor(Color.GREEN)
//
//            } else {
//                //change color back
//                med1Container.setBackgroundColor(Color.TRANSPARENT)
//            }
//        }

//        //show edit weight layout
//        addWeightBtn.setOnClickListener(){
//            if(editWeightLayout.visibility == View.VISIBLE){
//                editWeightLayout.visibility = View.GONE
//            }
//            else{
//                editWeightLayout.visibility = View.VISIBLE
//                moodLayout.visibility = View.GONE
//            }
//        }

        //edit weight
//        val currWeight = weight.text
//        weightInput.setText(currWeight)
//
//        weightUp.setOnClickListener(){
//            var weightNum = weightInput.text.toString().toInt()
//            weightNum += 1
//            weightInput.setText(weightNum.toString())
//        }
//        weightDown.setOnClickListener(){
//            var weightNum = weightInput.text.toString().toInt()
//            weightNum -= 1
//            weightInput.setText(weightNum.toString())
//        }
//        editWeightSubmit.setOnClickListener(){
//            val newWeight = weightInput.text
//            weight.text = newWeight
//            editWeightLayout.visibility = View.GONE
//        }

    } ////////

    //sets saved mood
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP) //requirement for getDrawable()
    fun setMood(view: View) {
        val errMessage = "This doesn't work yet >:("
        Snackbar.make(
                findViewById(R.id.secondaryContainer),
                errMessage,
                Snackbar.LENGTH_SHORT
        ).show()
//        moodLayout.visibility = View.GONE
//            val btnId = view.resources
//        Log.d("ID", btnId.toString())
//            savedMood.setImageDrawable(getDrawable(R.drawable.meh)) //TODO: this doesn't change the mood to the selected one atm
//        val drawble = resources.getDrawable(R.drawable.ic_training,theme)

//        val chosenMood = resources.getDrawable(R.drawable.tired, theme)
//                savedMood.setImageDrawable()
    }

}

