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
            //go to login activity
            val intent = Intent(this, Login::class.java)
            intent.putExtra("", "")
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
            //go to webpage, at some point will be Transcribe's own website
            //for now, go to my personal site -- I'll make a page for it off of my domain eventually
            val url = "https://www.paisleymdavis.com"
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