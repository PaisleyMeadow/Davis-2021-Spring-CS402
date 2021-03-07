package com.paisleydavis.transcribe

import android.widget.EditText
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @see: https://mailboxlayer.com/
 */
class EmailValidationService(userEmail: String) {
    private val service: EmailValidationAPI

    init{

        val url = "https://apilayer.net/api/"

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(EmailValidationAPI::class.java)
    }

    fun getValidationResponse(callback: Callback<EmailValidationResponse>){
        val call = service.getValidationResponse()
        call.enqueue(callback)
    }
}
