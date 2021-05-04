package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.findFragment
import com.google.android.material.internal.ContextUtils.getActivity

import com.paisleydavis.transcribe.dataClasses.TopicItem
import com.paisleydavis.transcribe.fragments.BarGraphFragment
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [TopicItem].
 * TODO: Replace the implementation with code for your data type.
 */
class CommunityTopicRecyclerViewAdapter(
        private var values: List<TopicItem>)
    : RecyclerView.Adapter<CommunityTopicRecyclerViewAdapter.ViewHolder>() {

    var allValues : List<TopicItem> = values
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_topic, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.contentView.text = item.name

        // I think this is where I'm able to assign events and values to items in the fragment
        // if I use variables assigned in the ViewHolder class

        // actually using cursed graph fragment code, because doing it in XML
        // didn't create a graph fragment for each list item (or made the loading weird, idk)
        val activity: AppCompatActivity = view.context as AppCompatActivity
        val graph = BarGraphFragment()
        activity.supportFragmentManager.beginTransaction().add(R.id.graph_container, graph).commit()

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
            intent.putExtra("topic", item.name)
            holder.contentView.context.startActivity(intent)
        }

        // snagged most of this code from: https://stackoverflow.com/a/60011574/9719413
//        var DELAY:Long = 2000
//        var timer = Timer()
//        holder.searchView.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                Log.e("TAG", "timer start")
//                timer = Timer()
//                timer.schedule(object : TimerTask() {
//                    override fun run() {
//                        //do something
//                        Log.d("SOME", "it's working???")
////                        TopicFragment().getAdapter().filter("s")
//                    }
//                }, DELAY)
//            }
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                Log.e("TAG","timer cancel ")
//                timer.cancel() //Terminates this timer, discarding any currently scheduled tasks.
//                timer.purge() //Removes all cancelled tasks from this timer's task queue.
//            }
//        })
    }

    override fun getItemCount(): Int = values.size

    // see: https://stackoverflow.com/questions/30398247/how-to-filter-a-recyclerview-with-a-searchview
    // filter for searching
    fun filter(text: String){
        values = allValues
        var filteredValues = values
            if(text.isNotEmpty()) {
                // filter list of values
                filteredValues = values.filter{it.name.toLowerCase().contains(text.toLowerCase())}
            }
        values = filteredValues

        notifyDataSetChanged()
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val contentView: TextView = view.findViewById(R.id.topic_title)

        // create my own view/element variables below; able to access main view
        val data: ConstraintLayout = view.findViewById(R.id.data_container)
        val button: ImageButton = view.findViewById(R.id.topic_add)
        val contribute: Button = view.findViewById(R.id.comm_contribute)
//        val activity: View = this.getActivity(view.context)
//        val searchView: EditText = view.rootView.findViewById(R.id.community_search)

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
}