package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.findFragment
import com.google.android.material.internal.ContextUtils.getActivity

import com.paisleydavis.transcribe.dummy.CommunityTopicRepository.TopicItem

/**
 * [RecyclerView.Adapter] that can display a [TopicItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CommunityTopicRecyclerViewAdapter(
        private val values: List<TopicItem>)
    : RecyclerView.Adapter<CommunityTopicRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_topic, parent, false)

        // actually using cursed graph fragment code, because doing it in XML
        // didn't create a graph fragment for each list item
        val activity: AppCompatActivity = view.getContext() as AppCompatActivity
        val graph = BarGraphFragment()
        activity.supportFragmentManager.beginTransaction().replace(R.id.graph_container, graph).commit()


        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.content

        // I think this is where I'm able to assign events and values to items in the fragment
        // if I use variables assigned in the ViewHolder class

        // hide data container underneath topic name
        holder.data.visibility = View.GONE

        // show and hide data container on button tap
        holder.button.setOnClickListener{
            if(holder.data.visibility == View.GONE){
                holder.data.visibility = View.VISIBLE
            }
            else{
                holder.data.visibility = View.GONE
            }
        }

        // click listener for contributing to community data on topic
        holder.contribute.setOnClickListener{
            val intent = Intent(holder.contentView.context, Contribute::class.java)
            intent.putExtra("topic", "DummyTopic")
            holder.contentView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = values.size

    // TODO: implement search on community topics
    // see: https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
    // filter for searching
    fun filter(text: String){
            val valuesCopy = values
            if(text.isEmpty()) {
                // filter list of values
//                val filteredValues = values.filter{it.length > 5}
            }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.findViewById(R.id.topic_title)

        // create my own view/element variables below; able to access main view
        val data: ConstraintLayout = view.findViewById(R.id.data_container)
        val button: ImageButton = view.findViewById(R.id.topic_add)
        val contribute: Button = view.findViewById(R.id.comm_contribute)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}