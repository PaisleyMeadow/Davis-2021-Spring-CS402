package com.paisleydavis.transcribe.dataClasses

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne

@Entity
data class MedData (
    @Id
    var id:Long = 0,
    var name:String = "",
    var dosageAmount:Long = 0,
    var dosageUnit:String = "",
    var frequencyDays:String = "",
    var reminderOn:Boolean = false,
    var reminderHour:Int = 0,
    var reminderMinute:Int = 0,
){
    //relation to User
    lateinit var user: ToOne<UserData> // for 1-to-many relation with user (user -> meds)
}