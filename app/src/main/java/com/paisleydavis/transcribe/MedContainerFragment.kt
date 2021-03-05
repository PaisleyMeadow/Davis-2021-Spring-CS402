package com.paisleydavis.transcribe

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.util.Log
import android.widget.Button
import kotlinx.android.synthetic.main.fragment_med.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MedContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MedContainerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val medFragment = MedFragment.newInstance("Lexapro", "10mg")
    private val medFragment2 = MedFragment.newInstance("Somethingoxyn", "100mg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_med_container, container, false)

        //place some filler meds into container (someday will get info from database, but ya know).
        if(savedInstanceState == null) {
            createNewMedFragment("Lexapro", "20mg")
            createNewMedFragment("Androidatilisol", "5mg")
        }

        val addButton = view.findViewById<Button>(R.id.addMedButton)
        addButton.setOnClickListener(showAddMed())

        // Inflate the layout for this fragment
        return view
    }

    /**
     * Show add med options
     */
    private fun showAddMed(): View.OnClickListener? {

    }

    /**
     * Create new medication fragment
     */
    private fun createNewMedFragment(name: String, dosage: String) {
        val newMedFragment = MedFragment.newInstance(name, dosage)

        //will have to worry about state here eventually (probably anyways)
        fragmentManager?.beginTransaction()
            ?.add(R.id.medContainerFrame, newMedFragment)
            ?.commit()

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