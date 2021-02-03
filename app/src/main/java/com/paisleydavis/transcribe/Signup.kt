package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupBtn = findViewById<Button>(R.id.signupConfirmBtn)
        signupBtn.setOnClickListener(){
            //change sign up activity
            val intent = Intent(this, Profile::class.java)

            val usernameInput = findViewById<EditText>(R.id.signupName)
            val passwordInput = findViewById<EditText>(R.id.signupPass1)
            val password2Input = findViewById<EditText>(R.id.signupPass2)

            if(usernameInput.text.toString().isEmpty() || passwordInput.text.toString().isEmpty() || password2Input.text.toString().isEmpty()){

                //the simplest way I found to hide the soft keyboard (because it's weirdly difficult to)
                (currentFocus ?: View(this))
                    .apply { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(windowToken, 0) }

                val errMessage = "Please fill out your information before continuing."
                Snackbar.make(
                    findViewById(R.id.signupLayout),
                    errMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else{
                intent.putExtra("username", usernameInput.text.toString())
                startActivity(intent)
            }

        }
    }
}