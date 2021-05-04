package com.paisleydavis.transcribe.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.paisleydavis.transcribe.Community
import com.paisleydavis.transcribe.Profile
import com.paisleydavis.transcribe.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BottomBarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BottomBarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //configure click events
    private val myOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.home_icon -> {
                if(activity?.javaClass?.simpleName.toString() != "Profile") {
                    val intent = Intent(activity, Profile::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                }
                true
            }
            R.id.community_icon -> {
                if(activity?.javaClass?.simpleName.toString() != "Community") {
                    val intent = Intent(activity, Community::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent)
                }
                true
            }
        }

        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewOfLayout = inflater.inflate(R.layout.fragment_bottom_bar, container, false)

        //initialize bottom bar
        val bottomNavigation: BottomNavigationView = viewOfLayout.findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(myOnNavigationItemSelectedListener)

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BottomBarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BottomBarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}