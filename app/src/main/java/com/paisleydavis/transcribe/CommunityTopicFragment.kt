package com.paisleydavis.transcribe

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val button = viewOfLayout.findViewById<ImageButton>(R.id.topic_add)
        button.setOnClickListener{
            if (data.visibility == View.GONE){ Log.d("SHOW", "Show data...")
                data.visibility = View.VISIBLE
            }
            else{ Log.d("HIDE", "Show data...")
                data.visibility = View.GONE
            }
        }

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
         * @return A new instance of fragment CommunityTopicFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityTopicFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}