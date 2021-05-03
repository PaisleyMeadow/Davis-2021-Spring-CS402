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
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.dataClasses.UserData_
import com.paisleydavis.transcribe.emailValidation.EmailValidationResponse
import com.paisleydavis.transcribe.emailValidation.EmailValidationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val signupBtn = findViewById<Button>(R.id.signUpPageBtn)
        signupBtn.setOnClickListener(){
            //change sign up activity
            val intent = Intent(this, Profile::class.java)

            val usernameInput = findViewById<EditText>(R.id.signupName)
            val userEmail = findViewById<EditText>(R.id.signupEmail)
            val passwordInput = findViewById<EditText>(R.id.signupPass1)
            val password2Input = findViewById<EditText>(R.id.signupPass2)

            if(usernameInput.text.toString().isEmpty() || passwordInput.text.toString().isEmpty() || password2Input.text.toString().isEmpty()){

                //the simplest way I found to hide the soft keyboard (because it's weirdly difficult to)
                (currentFocus ?: View(this))
                    .apply { (getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
                        .hideSoftInputFromWindow(windowToken, 0) }

                val errMessage = "Please fill out your information before continuing."
                displayErrorMessage(errMessage)
            }
            else if(passwordInput.text.toString() != password2Input.text.toString()){
                val errMessage = "Passwords do not match."
                displayErrorMessage(errMessage)
            } // check to see if username already exists in db
            else if(ObjectBox.boxStore.boxFor(UserData::class.java).query().equal(UserData_.username, usernameInput.text.toString()).build().find().isNotEmpty()){
                displayErrorMessage("Username already taken.")
            }
            else{

                //check if email is valid using validation api
                val service = EmailValidationService()

                val callback = object: Callback<EmailValidationResponse> {
                    override fun onFailure(call: Call<EmailValidationResponse>, t: Throwable) {
                        displayErrorMessage("Error validating email, please try again.")
                    }

                    override fun onResponse(
                        call: Call<EmailValidationResponse>,
                        response: Response<EmailValidationResponse>
                    ) {
                        response.isSuccessful.let{
                            //response contains recommendations on typos, MX-Record look-up,
                            //and a general quality score
                            val resp = response.body()

                            //if user inputted email is not found in look-up or doesn't have a high
                            //enough quality score, an error is displayed
                            if(resp?.mxFound == false || resp?.score!! < 0.64){
                                var errorMessage = "Email is not valid."

                                //spelling suggestion from API is listed in error if available
                                if(resp.didYouMean != "" && resp.didYouMean != null) {
                                    errorMessage += " Did you mean " + resp.didYouMean + "?"
                                }
                                displayErrorMessage(errorMessage)
                            }
                            else{ //otherwise, user data is accepted and goes to Profile activity
                                intent.putExtra("username", usernameInput.text.toString())

                                // create user in db
                                addUser(usernameInput.text.toString(), userEmail.text.toString(), passwordInput.text.toString())

                                intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
                                startActivity(intent)
                            }
                        }
                    }
                }
                service.getValidationResponse(userEmail.text.toString(), callback)
            }

        }
    }

    /**
     * add user data to database
     */
    private fun addUser(username: String, email: String, password: String) {

        val newUserData = UserData(username = username, password = password)

        // set global user
        TranscribeApplication.setUser(newUserData)
    }

    private fun displayErrorMessage(message: String) {
        Snackbar.make(
            findViewById(R.id.signupLayout),
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}