package com.paisleydavis.transcribe

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Just using 4 values right now, but saving the others just in case
 */
data class EmailValidationResponse (
    val email: String,
    @SerializedName("did_you_mean")
    val didYouMean: String,
//    @SerializedName("format_valid")
//    val formatValid: String,
    @SerializedName("mx_found")
    val mxFound: String,
//    @SerializedName("smtp_check")
//    val smptpCheck: String,
//    @SerializedName("catch_all")
//    val catchAll: String,
//    val role: String,
//    val disposable: String,
//    val free: String,
    val score: Float
)