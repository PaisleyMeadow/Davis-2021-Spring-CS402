package com.paisleydavis.transcribe.emailValidation

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * "email" param is passed in at service.getValidationResponse in Signup -> EmailValidationService
 * -> here
 */
interface EmailValidationAPI {
    @GET("check?access_key=4fda4dd2c3a4be5999660c72d27587e8")
    fun getValidationResponse(@Query("email") email: String): Call<EmailValidationResponse>
}
