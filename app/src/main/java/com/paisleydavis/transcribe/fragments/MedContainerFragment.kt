package com.paisleydavis.transcribe.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.paisleydavis.transcribe.*
import com.paisleydavis.transcribe.ObjectBox.boxStore
import com.paisleydavis.transcribe.dataClasses.MedData
import com.paisleydavis.transcribe.dataClasses.NewMedEvent
import com.paisleydavis.transcribe.dataClasses.UserData
import io.objectbox.kotlin.boxFor
import io.objectbox.Box
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "medName"
private const val ARG_PARAM2 = "medDosage"

/**
 * A simple [Fragment] subclass.
 * Use the [MedContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedContainerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var viewOfLayout: View

//    private val medFragment = MedFragment.newInstance("Lexapro", "10mg")
//    private val medFragment2 = MedFragment.newInstance("Somethingoxyn", "100mg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        //register eventbus for observing adding new medications
        EventBus.getDefault().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_med_container, container, false)

        //hide meds
        val medContainer = viewOfLayout.findViewById<LinearLayout>(R.id.medContainerFrame)
        medContainer.visibility = View.GONE


        //load meds from object box that belong to current user
        val res = TranscribeApplication.getUser().meds

        if(res.isNotEmpty()){
            val emptyText = viewOfLayout.findViewById<TextView>(R.id.emptyMedText)
            emptyText.visibility = View.GONE

            for(med in res){
                createNewMedFragment(med.name, med.dosageAmount, med.dosageUnit, med.frequencyDays, med.reminderOn, med.reminderHour, med.reminderMinute)
            }
        }


        val addButton = viewOfLayout.findViewById<ImageView>(R.id.addMedButton)
        addButton.setOnClickListener{goToAddMed()}

        val expandButton = viewOfLayout.findViewById<ImageButton>(R.id.expand_med)
        expandButton.setOnClickListener{
            if(medContainer.visibility == View.GONE){
                medContainer.visibility = View.VISIBLE
                expandButton.setBackgroundResource(R.drawable.ic_expand_more)
            }
            else{
                medContainer.visibility = View.GONE
                expandButton.setBackgroundResource(R.drawable.ic_expand_less)
            }
        }

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: NewMedEvent){
        //hide default med container text
        val emptyMedText = viewOfLayout.findViewById<TextView>(R.id.emptyMedText)
        emptyMedText.visibility = View.GONE

        //show meds in dropdown so user will see new med added when add med returns
        val dropdown = viewOfLayout.findViewById<ImageButton>(R.id.expand_med)
        dropdown.performClick()

        //create new med fragment
        createNewMedFragment(event.medName, event.medDosage, event.medUnit, event.medFrequency, event.medReminder, event.medHour, event.medMinute)

        //add med to objectbox
        val newMedData = MedData(name = event.medName,
            dosageAmount = event.medDosage, dosageUnit = event.medUnit, frequencyDays = event.medFrequency,
            reminderOn = event.medReminder, reminderHour = event.medHour, reminderMinute = event.medMinute)
        // associate with user
        newMedData.user.target = TranscribeApplication.getUser()
        TranscribeApplication.getUser().meds.add(newMedData)
        boxStore.boxFor(UserData::class.java).put(TranscribeApplication.getUser())

        boxStore.boxFor(MedData::class.java).put(newMedData)
        val medBox: Box<MedData> = boxStore.boxFor()
        medBox.put(newMedData)
//        val results = boxStore.boxFor(MedData::class.java).all
//        Log.d("MEDS", results.toString())
//        Log.d("CURRENT", TranscribeApplication.getUser().toString())
    }

    /**
     * Go to add med activity, from profile by tapping medications + button
     */
    private fun goToAddMed(){
        val intent = Intent(activity, AddMedActivity::class.java)
        startActivity(intent)
    }

    /**
     * Create new medication fragment
     */
    private fun createNewMedFragment(name: String, dosage: Long, unit: String, freq: String, reminder: Boolean, hour: Int, minute: Int) {
        val newMedFragment = MedFragment.newInstance(name, dosage, unit, freq, reminder, hour, minute)

        //will have to worry about state here eventually (probably anyways)
        //TODO: ask Michael about fragmentManager deprecation
        parentFragmentManager.beginTransaction()
            .add(R.id.medContainerFrame, newMedFragment)
            .commitAllowingStateLoss();
            //TODO: ask Michael about the fragment/state loss issue^ (replicate by changing
            //to just ".commit()"
            //is commitAllowingStateLoss() bad? Maybe. Did it fix my problem? Yes.

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MedContainerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MedContainerFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onStop() {
        super.onStop()
//        //temp delete meds
//        ObjectBox.boxStore.close()
//        ObjectBox.boxStore.deleteAllFiles()
    }
}