package com.paisleydavis.transcribe.dataClasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class MedData (
    @Id
    var id:Long = 0,
    val name:String = "",
    val dosageAmount:Long = 0,
    val dosageUnit:String = "",
    val frequencyDays:String = "",
    val reminderOn:Boolean = false,
    val reminderHour:Int = 0,
    val reminderMinute:Int = 0,
){
    //relation to User
    lateinit var user: ToOne<UserData> // for 1-to-many relation with user (user -> meds)
}