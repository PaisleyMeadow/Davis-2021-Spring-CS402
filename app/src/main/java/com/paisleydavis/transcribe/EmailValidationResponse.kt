package com.paisleydavis.transcribe

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Just using 4 values right now, but saving the others just in case
 * @property didYouMean - spellcheck suggestion (".sol" -> ".aol"
 * @property mxFound - returns true if email is found in MX-Records lookup
 * @property score - "quality score" as decided by mailboxlayer
 * @link https://mailboxlayer.com/documentation
 */
data class EmailValidationResponse (
    val email: String,
    @SerializedName("did_you_mean")
    val didYouMean: String,
//    @SerializedName("format_valid")
//    val formatValid: String,
    @SerializedName("mx_found")
    val mxFound: Boolean,
//    @SerializedName("smtp_check")
//    val smptpCheck: String,
//    @SerializedName("catch_all")
//    val catchAll: String,
//    val role: String,
//    val disposable: String,
//    val free: String,
    val score: Float
)