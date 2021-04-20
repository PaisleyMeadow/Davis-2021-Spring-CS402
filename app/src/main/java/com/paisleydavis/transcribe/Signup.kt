package com.paisleydavis.transcribe

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
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
                Snackbar.make(
                    findViewById(R.id.signupLayout),
                    errMessage,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            else{ //TODO: implement password requirements/compare both passwords

                //check if email is valid using validation api
                val service = EmailValidationService()

                val callback = object: Callback<EmailValidationResponse> {
                    override fun onFailure(call: Call<EmailValidationResponse>, t: Throwable) {
                        Log.e("EMAIL_VALIDATION", "Error")
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
                                if(resp.didYouMean != "") {
                                    errorMessage += " Did you mean " + resp.didYouMean + "?"
                                }
                                displayErrorMessage(errorMessage)
                            }
                            else{ //otherwise, data is accepted and goes to Profile activity
                                intent.putExtra("username", usernameInput.text.toString())
                                startActivity(intent)
                            }
                        }
                    }
                }
                service.getValidationResponse(userEmail.text.toString(), callback)
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