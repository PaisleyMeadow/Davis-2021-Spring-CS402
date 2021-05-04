package com.paisleydavis.transcribe

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.paisleydavis.transcribe.fragments.BottomBarFragment
import com.paisleydavis.transcribe.fragments.TopicFragment
import java.util.*

class Community : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        //initialize bottom menu bar fragment
        val bottomBar = BottomBarFragment.newInstance("", "")
        supportFragmentManager.beginTransaction()
            .replace(R.id.community_bottom_bar, bottomBar)
            .commit();

        // initialize list topic fragment
        supportFragmentManager.beginTransaction()
            .add(R.id.community_search_frame_layout, TopicFragment())
            .commit()
    }
}