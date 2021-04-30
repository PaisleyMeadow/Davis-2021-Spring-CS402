package com.paisleydavis.transcribe.dataClasses

import java.util.*

data class NewMedEvent (
    val medName:String,
    val medDosage:Long,
    val medUnit:String,
    val medFrequency:String,
    val medReminder:Boolean,
    val medHour:Int,
    val medMinute: Int
)