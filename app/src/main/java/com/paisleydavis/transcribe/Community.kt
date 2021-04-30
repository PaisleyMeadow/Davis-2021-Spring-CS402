package com.paisleydavis.transcribe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.paisleydavis.transcribe.fragments.BottomBarFragment
import com.paisleydavis.transcribe.fragments.TopicFragment

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

    private fun createTopicFragment(topicTitle: String) {
//        val topicFragment = CommunityTopicFragment.newInstance(topicTitle, "")
//        supportFragmentManager.beginTransaction()
//            .add(R.id.categories_layout, topicFragment)
//            .commit();
    }
}