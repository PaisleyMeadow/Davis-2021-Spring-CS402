package com.paisleydavis.transcribe

import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.iterator
import com.paisleydavis.transcribe.ObjectBox.boxStore
import com.paisleydavis.transcribe.dataClasses.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.w3c.dom.Text
import java.util.*

/**
 * Where "+" add medication button on Profile activity takes you to.
 * A form where user inputs info about a new med/supplement, which will then return to the Profile
 * activity, and notify MedContainerFragment to create a new MedFragment
 */
class AddMedActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener{

    private lateinit var adapter : ArrayAdapter<CharSequence>
    var isEdit : Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med)

        var currMed = MedData()

        // if editing med, get data from db and fill in sections accordingly
        if(intent.extras?.containsKey("edit") == true){
            isEdit = true
            val user =
                boxStore.boxFor(UserData::class.java).get(TranscribeApplication.getUser().id)
            val meds = user.meds
            for(med in meds){
                if(intent.extras?.get("medName") == med.name){
                    currMed = med
                }
            }
        }

        val nameTextView = findViewById<TextView>(R.id.addMedName)
        val dosageTextView = findViewById<TextView>(R.id.addMedDosage)

        if(isEdit){
            nameTextView.text = currMed.name
            dosageTextView.text = currMed.dosageAmount.toString()
        }

        //populate units in spinner
        val unitSpinner = findViewById<Spinner>(R.id.units_spinner)
        adapter = ArrayAdapter.createFromResource(
                this,
                R.array.units_array,
                android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            unitSpinner.adapter = adapter
        }
        unitSpinner.onItemSelectedListener = this

        if(isEdit){
            val pos = adapter.getPosition(currMed.dosageUnit)
            unitSpinner.setSelection(pos)
        }

        // initialize time picker and hide it
        var timePicker = findViewById<TimePicker>(R.id.medTimePicker)
        timePicker.visibility = View.GONE

        // on reminder switch, show time picker
        var reminderSwitch = findViewById<Switch>(R.id.reminderSwitch)
        reminderSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                timePicker.visibility = View.VISIBLE
            }
            else{
                timePicker.visibility = View.GONE
            }
        }

        if(isEdit){
            timePicker.hour = currMed.reminderHour
            timePicker.minute = currMed.reminderMinute
            reminderSwitch.isChecked = true
        }

        // create an array of all checkboxes so they can be checked later
        // top row of checkboxes
        val checkboxes: ArrayList<CheckBox> = ArrayList()
        val checkLayoutTop = findViewById<LinearLayout>(R.id.checkLayoutTop)
        for(c in checkLayoutTop){
            val box: CheckBox = c as CheckBox
            checkboxes.add(box)
        }
        // bottom row of checkboxes
        val checkLayoutBottom = findViewById<LinearLayout>(R.id.checkLayoutBottom)
        for(c in checkLayoutBottom){
            val box: CheckBox = c as CheckBox
            checkboxes.add(box)
        }

        //on checking "every day" checkbox, checks all the other weekday boxes
        val everydayCheck = findViewById<CheckBox>(R.id.everydayCheck)
        everydayCheck.setOnClickListener{
            for(box in checkboxes){
                box.isChecked = everydayCheck.isChecked
            }
        }

        // unchecking a checkbox should uncheck the "every day" box
        for(box in checkboxes){
            if(box != everydayCheck) {
                box.setOnClickListener {
                    if (!box.isChecked) {
                        everydayCheck.isChecked = false
                    }
                }
            }
        }

        if(isEdit){
            if(currMed.frequencyDays.contains("Every")){
                everydayCheck.performClick()
            }
            else{
                for(box in checkboxes){
                    if(currMed.frequencyDays.contains(box.text.toString())){
                        box.isChecked = true
                    }
                }
            }
        }


        // On add button, triggers event bus listener
        val addButton = findViewById<Button>(R.id.addNewMedButton)
        addButton.setOnClickListener{
            val nameText = nameTextView.text.toString()
            val dosageText = dosageTextView.text.toString()

            val dosageUnit = unitSpinner.selectedItem

            // gets checked weekdays
            val selectedDays: ArrayList<String> = ArrayList()
            for(day in checkboxes){
                if(day.isChecked) {
                    selectedDays.add(day.text as String)
                }
            }

            var reminderHour = 0
            var reminderMinute = 0

            if(reminderSwitch.isChecked){
                reminderHour = timePicker.hour
                reminderMinute = timePicker.minute
            }

            if(nameText != "" && dosageText != ""){

                // if editing existing med, need to replace old fragment and change in db
                if(isEdit){
                    val medBox = boxStore.boxFor(MedData::class.java)
                    var oldMed = medBox.query().equal(MedData_.name, nameText).equal(MedData_.userId, TranscribeApplication.getUser().id).build().findFirst()
                    oldMed?.name = nameText
                    oldMed?.dosageAmount = dosageText.toLong()
                    oldMed?.dosageUnit = dosageUnit.toString()
                    oldMed?.frequencyDays = selectedDays.toString()
                    oldMed?.reminderOn = reminderSwitch.isChecked
                    oldMed?.reminderHour = reminderHour
                    oldMed?.reminderMinute = reminderMinute

                    // add back into box to update
                    medBox.put(oldMed)
                    TranscribeApplication.getUser().meds =
                        boxStore.boxFor(UserData::class.java).query().equal(
                            UserData_.id, TranscribeApplication.getUser().id).build().findFirst()?.meds
                            ?: TranscribeApplication.getUser().meds

                    // remove old fragment
                    supportFragmentManager
                }

                //trigger event bus observer in MedContainerFragment
                EventBus.getDefault().post(NewMedEvent(nameText, dosageText.toLong(), dosageUnit as String, selectedDays.toString(), reminderSwitch.isChecked, reminderHour, reminderMinute))

                //finish this activity
                finish()
            }
            else{

                var errMessage = ""
                if(nameText == ""){
                    errMessage = "Please enter a medication or supplement name."
                }
                else if(dosageText == ""){
                    errMessage = "Please enter a dosage for this medication or supplement."
                }

                showError(errMessage)
            }
        }
    }

    private fun showError(errMessage: String){
        Snackbar.make(
                findViewById(R.id.OuterAddMedLayout),
                errMessage,
                Snackbar.LENGTH_SHORT
        ).show()
    }

    // extending interface for unit spinner
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // not necessary to implement at the moment
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
       // not necessary to implement at the moment
    }
}

