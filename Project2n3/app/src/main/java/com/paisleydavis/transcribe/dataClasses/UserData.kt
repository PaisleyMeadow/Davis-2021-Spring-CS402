package com.paisleydavis.transcribe.dataClasses
import com.paisleydavis.transcribe.dataClasses.MedData
import io.objectbox.annotation.Backlink
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Unique
import io.objectbox.relation.ToMany

// TODO: this should be in a firebase db or something, but for now...
@Entity
data class UserData (
    @Id
    var id:Long = 0,
    @Unique
    var username:String = "",
    var email:String = "",
    var password:String,
    var theme:String = "default"
){
    @Backlink(to = "user")
    lateinit var meds: ToMany<MedData> // store all meds uploaded by user
}