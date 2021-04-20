package com.paisleydavis.transcribe

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.w3c.dom.Text

class Login : AppCompatActivity() {
    //TODO: implement the logging in lol - might do Firebase db, idk yet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginPageBtn)
        loginBtn.setOnClickListener{
            //get username to pass in intent
            val username = findViewById<TextView>(R.id.loginName)
            Log.d("LOGINNAME", username.text.toString())
            //change to profile activity
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("username", username.text.toString())

            startActivity(intent)
        }
    }
}