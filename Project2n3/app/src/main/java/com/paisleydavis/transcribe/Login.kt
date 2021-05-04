package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.dataClasses.UserData_
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class Login : AppCompatActivity() {
    //TODO: implement the logging in (lol) - might do Firebase db, idk yet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginBtn = findViewById<Button>(R.id.loginPageBtn)
        loginBtn.setOnClickListener{
            //get username  and password as strings
            val username = findViewById<TextView>(R.id.loginName).text.toString()
            val password = findViewById<TextView>(R.id.loginPass).text.toString()


            if(username.isEmpty() || password.isEmpty()){
                //the simplest way I found to hide the soft keyboard (because it's weirdly difficult to)
                (currentFocus ?: View(this))
                        .apply { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                                .hideSoftInputFromWindow(windowToken, 0) }
                displayErrorMessage("Please provide your account information.")
            }
            else {

                // check if this user exists
                val userBox: Box<UserData> = ObjectBox.boxStore.boxFor()
                val query = userBox.query().run {
                    equal(UserData_.username, username)
                    equal(UserData_.password, password)
                    order(UserData_.username)
                    build()
                }

                val user = query.findUnique()
                if (user != null) {
                    // set global user
                    TranscribeApplication.setUser(user)

                    // change to profile activity
                    val intent = Intent(this, Profile::class.java)
                    intent.putExtra("username", username)

                    startActivity(intent)
                }
                else{
                    displayErrorMessage("User not found.")
                }
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