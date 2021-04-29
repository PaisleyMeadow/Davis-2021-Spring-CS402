package com.paisleydavis.transcribe

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "topicTitle"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityTopicFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityTopicFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var topicTitle: String? = null
    private var param2: String? = null
    lateinit private var viewOfLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            topicTitle = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewOfLayout = inflater.inflate(R.layout.fragment_community_topic, container, false)

        val title = viewOfLayout.findViewById<TextView>(R.id.topic_title)
        title.text = topicTitle

        //hide data container
        val data = viewOfLayout.findViewById<ConstraintLayout>(R.id.data_container)
        data.visibility = View.GONE

        //create graph in data container
        val graph = BarGraphFragment.newInstance("", "")
        parentFragmentManager.beginTransaction()
                .replace(R.id.graph_container, graph)
                .commit();


        //button display data container
        val button = viewOfLayout.findViewById<ImageButton>(R.id.topic_add)
        button.setOnClickListener{
            if (data.visibility == View.GONE){
                data.visibility = View.VISIBLE
            }
            else{
                data.visibility = View.GONE
            }
        }

        //event for "contribute" activity
        val contribute = viewOfLayout.findViewById<Button>(R.id.comm_contribute)
        contribute.setOnClickListener{
            // go to contribute activity on button press
            val intent = Intent(activity, Contribute::class.java)
            // pass title of topic to contribute activity
            intent.putExtra("topic", topicTitle.toString())
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return viewOfLayout
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param topicTitle Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityTopicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(topicTitle: String, param2: String) =
            CommunityTopicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, topicTitle)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}