package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import org.w3c.dom.Text

class Login : AppCompatActivity() {
    //TODO: implement the logging in (lol) - might do Firebase db, idk yet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginPageBtn)
        loginBtn.setOnClickListener{
            //get username to pass in intent
            val username = findViewById<TextView>(R.id.loginName).text
            val password = findViewById<TextView>(R.id.loginPass).text

            if(username.toString().isEmpty() || password.toString().isEmpty()){
                //the simplest way I found to hide the soft keyboard (because it's weirdly difficult to)
                (currentFocus ?: View(this))
                        .apply { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(windowToken, 0) }
                displayErrorMessage("Please provide your account information.")
            }
            else {
                //change to profile activity
                val intent = Intent(this, Profile::class.java)
                intent.putExtra("username", username.toString())

                startActivity(intent)
            }
        }
    }

    private fun displayErrorMessage(message: String) {
        Snackbar.make(
                findViewById(R.id.signupLayout),
                message,
                Snackbar.LENGTH_LONG
        ).show()
    }
}