package com.paisleydavis.transcribe

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginBtn = findViewById<Button>(R.id.loginBtn)
        loginBtn.setOnClickListener(){
            //change to profile activity
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("logintmp", "testing main to signup")

            startActivity(intent)
        }

        val signupBtn = findViewById<Button>(R.id.signupBtn)
        signupBtn.setOnClickListener(){
            //change to sign up activity
            val intent = Intent(this, Signup::class.java)
            intent.putExtra("signuptmp", "testing main to signup")

            startActivity(intent)
        }

        val helpBtn = findViewById<ImageView>(R.id.helpBtn)
        helpBtn.setOnClickListener(){
            //go to webpage
            //this is a filler link lol
            //idk if I'll have it go to another page w/ info or my website. might have about button for website
            val url = "https://hooooooooo.com/"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }


    override fun onStop() {
        super.onStop()
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}