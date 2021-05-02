package com.paisleydavis.transcribe.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TableRow
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.paisleydavis.transcribe.AddMedActivity
import com.paisleydavis.transcribe.ObjectBox
import com.paisleydavis.transcribe.R
import com.paisleydavis.transcribe.TranscribeApplication
import com.paisleydavis.transcribe.dataClasses.MedData
import com.paisleydavis.transcribe.dataClasses.MedData_
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.dataClasses.UserData_
import io.objectbox.kotlin.boxFor
import io.objectbox.relation.ToMany

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_MED_NAME = "medName"
private const val ARG_MED_DOSAGE = "medDosage"
private const val ARG_MED_UNIT = "medUnit"
private const val ARG_FREQUENCY = "frequency"
private const val ARG_REMINDER_ON = "reminderOn"
private const val ARG_REMINDER_HOUR = "reminderHour"
private const val ARG_REMINDER_MINUTE = "reminderMinute"

/**
 * A simple [Fragment] subclass.
 * Use the [MedFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var medName: String? = null
    private var medDosage: Long = 0
    private var medUnit: String? = null
    private var frequency: String? = null
    private var reminderOn: Boolean = false
    private var reminderHour: Int = 0
    private var reminderMinute: Int = 0

    private lateinit var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            medName = it.getString(ARG_MED_NAME)
            medDosage = it.getLong(ARG_MED_DOSAGE)
            medUnit = it.getString(ARG_MED_UNIT)
            frequency = it.getString(ARG_FREQUENCY)
            reminderOn = it.getBoolean(ARG_REMINDER_ON)
            reminderHour = it.getInt(ARG_REMINDER_HOUR)
            reminderMinute = it.getInt(ARG_REMINDER_MINUTE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_med, container, false)

        // fill in data to display in med fragment

        changeMedName(medName)
        changeMedDosage(medDosage, medUnit)
        // add reminder time if on, otherwise hide
        changeReminderTime(reminderOn, reminderHour, reminderMinute)
        // change the color of the appropriate weekday bubbles
        changeWeekdays(frequency)

        //set click listener for edit med button
        val editButton = viewOfLayout.findViewById<ImageView>(R.id.edit_med)
        editButton.setOnClickListener{
            val intent = Intent(activity, AddMedActivity::class.java)
            intent.putExtra("edit", true)
            intent.putExtra("medName", medName)
            startActivity(intent)

            // delete fragment and med from db
//            parentFragmentManager.beginTransaction().remove(this).commit()
//            val medBox = ObjectBox.boxStore.boxFor(MedData::class.java)
//            val med = medBox.query().equal(MedData_.userId, TranscribeApplication.getUser().id).equal(MedData_.name, medName.toString())
//                .build().find()
//            medBox.query().equal(MedData_.userId, TranscribeApplication.getUser().id).equal(MedData_.name, medName.toString())
//                .build().remove()
//            // don't forget user to med relation...
//            TranscribeApplication.getUser().meds.remove(med)
//            TranscribeApplication.getUser().meds.applyChangesToDb()
        }

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    /**
     * takes string of chosen weekdays for medication and marks the appropriate weekday "bubbles" in the fragment
     */
    private fun changeWeekdays(frequency: String?) {
        val dayList = frequency?.replace("[", "")?.replace("]", "")?.split(", ")
        val container = viewOfLayout.findViewById<TableRow>(R.id.weekdayContainer)
        val temp = container.children

        // this could probably be done better buuuut whatevs

        val bubbles = arrayListOf<TextView>()
        for(t in temp){
            val b = t as TextView
            bubbles.add(b)

        }

        if (dayList != null) {
            for(chosenDay in dayList){
                when(chosenDay.substring(0, 2)){
                    "Su" -> {
                        val b = bubbles.find{ it.text.toString().equals("Su")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "Mo" -> {
                        val b = bubbles.find{ it.text.toString().equals("M")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "Tu" -> {
                        val b = bubbles.find{ it.text.toString().equals("T")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "We" -> {
                        val b = bubbles.find{ it.text.toString().equals("W")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "Th" -> {
                        val b = bubbles.find{ it.text.toString().equals("Th")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "Fr" -> {
                        val b = bubbles.find{ it.text.toString().equals("F")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                    "Sa" -> {
                        val b = bubbles.find{ it.text.toString().equals("S")}
                        b?.setBackgroundResource(R.drawable.ic_circle)
                    }
                }
            }

        }
    }

    private fun changeReminderTime(reminderOn: Boolean, reminderHour: Int, reminderMinute: Int) {
        val time = viewOfLayout.findViewById<TextView>(R.id.reminderTime)
        val switch = viewOfLayout.findViewById<Switch>(R.id.fragment_reminder_switch)
        var timeStr = ""
        var periodStr = ""
        if(reminderOn){
            // hour is in 24-hour format, so gotta change that, and decide am/pm
            if(reminderHour > 12){
                timeStr = (reminderHour - 12).toString()
                periodStr = "PM"
            }
            else{
                timeStr = reminderHour.toString()
                periodStr = "AM"
            }
            // add leading 0 if necessary
            timeStr += if(reminderMinute < 10){
                ":0$reminderMinute"
            } else{
                ":$reminderMinute"
            }

            time.text = "$timeStr $periodStr"
            switch.isChecked = true
        }
        else{
            time.visibility = View.GONE
        }

        switch.setOnClickListener{
            if(time.visibility == View.GONE){
                time.visibility = View.VISIBLE
            }
            else{
                time. visibility = View.GONE
            }
        }
    }

    private fun changeMedDosage(dosage: Long?, unit: String?) {
        val medDosageTextView = viewOfLayout.findViewById<TextView>(R.id.medDosageTextView)
        medDosageTextView.text = "${dosage.toString()} $unit"
    }

    private fun changeMedName(name: String?) {
        val medNameTextView = viewOfLayout.findViewById<TextView>(R.id.medNameTextView)
        medNameTextView.text = name
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(medName: String, medDosage: Long, medUnit: String, frequency: String, reminderOn: Boolean, reminderHour: Int, reminderMinute: Int) =
                MedFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_MED_NAME, medName)
                        putLong(ARG_MED_DOSAGE, medDosage)
                        putString(ARG_MED_UNIT, medUnit)
                        putString(ARG_FREQUENCY, frequency)
                        putBoolean(ARG_REMINDER_ON, reminderOn)
                        putInt(ARG_REMINDER_HOUR, reminderHour)
                        putInt(ARG_REMINDER_MINUTE, reminderMinute)
                    }
                }
    }
}

