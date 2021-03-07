package com.paisleydavis.transcribe

import retrofit2.Call
import retrofit2.http.GET

interface EmailValidationAPI {
    @GET("check?access_key=4fda4dd2c3a4be5999660c72d27587e8&email=pai@fat.com")
    fun getValidationResponse(): Call<EmailValidationResponse>
}
