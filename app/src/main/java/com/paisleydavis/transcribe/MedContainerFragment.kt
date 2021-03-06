package com.paisleydavis.transcribe

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_med.*
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

        val viewOfLayout = inflater.inflate(R.layout.fragment_med_container, container, false)

        //place some filler med fragments into container (someday will get info from database, but ya know).
        if(savedInstanceState == null) {
            createNewMedFragment("Lexapro", "20mg")
            createNewMedFragment("Androidatilisol", "5mg")
        }

        val addButton = viewOfLayout.findViewById<ImageView>(R.id.addMedButton)
        addButton.setOnClickListener{goToAddMed()}

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event:NewMedEvent){
        //create new med fragment
        createNewMedFragment(event.medName, event.medDosage)
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
    private fun createNewMedFragment(name: String, dosage: String) {
        val newMedFragment = MedFragment.newInstance(name, dosage)

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
}