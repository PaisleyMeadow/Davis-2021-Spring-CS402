package com.paisleydavis.transcribe.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.paisleydavis.transcribe.CommunityTopicRecyclerViewAdapter
import com.paisleydavis.transcribe.R
import com.paisleydavis.transcribe.dataClasses.TopicItem
import java.util.*
import kotlin.collections.ArrayList


/**
 * A fragment representing a list of Items.
 */
class TopicFragment : Fragment() {

    private var columnCount = 1
    lateinit var topicAdapter: CommunityTopicRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_topic_list, container, false)

        // Set the adapter
        if(view is RecyclerView){
            val items = createTopicArray(requireContext().resources.getStringArray(R.array.topics_array))
            topicAdapter = CommunityTopicRecyclerViewAdapter(items)

            view.layoutManager = LinearLayoutManager(context)
            view.adapter = topicAdapter
            recyclerView = view
        }

        // filter shown topics using search input
        // see: https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
        val searchView = activity?.findViewById<SearchView>(R.id.community_search)
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                topicAdapter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                topicAdapter.filter(newText)
                return true
            }
        })


        return view
    }

    private fun createTopicArray(arr: Array<String>): List<TopicItem> {
            val topicArr = ArrayList<TopicItem>()
            for( i in arr){
                val newTopicItem = TopicItem(i, "")
                topicArr.add(newTopicItem)
            }

        return topicArr
    }


    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
                TopicFragment().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_COLUMN_COUNT, columnCount)
                    }
                }
    }
}