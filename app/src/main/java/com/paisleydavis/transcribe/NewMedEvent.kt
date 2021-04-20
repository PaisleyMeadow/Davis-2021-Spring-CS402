package com.paisleydavis.transcribe

import java.util.*

data class NewMedEvent (
    val medName:String,
    val medDosage:Long,
    val medUnit:String,
    val medFrequency:ArrayList<String>,
    val medReminder:Boolean,
    val medHour:Int,
    val medMinute: Int
)