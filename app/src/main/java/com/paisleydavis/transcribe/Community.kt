package com.paisleydavis.transcribe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Community : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_community)

        //initialize bottom bar fragment
        val bottomBar = BottomBarFragment.newInstance("", "")
        supportFragmentManager.beginTransaction()
            .replace(R.id.community_bottom_bar, bottomBar)
            .commit();

        //initialize some filler community topic fragments
        createTopicFragment("Testosterone")
    }

    private fun createTopicFragment(topicTitle: String) {
        val topicFragment = CommunityTopicFragment.newInstance(topicTitle, "")
        supportFragmentManager.beginTransaction()
            .add(R.id.categories_layout, topicFragment)
            .commit();
    }
}