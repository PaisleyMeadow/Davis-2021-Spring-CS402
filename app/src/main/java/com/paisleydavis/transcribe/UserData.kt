package com.paisleydavis.transcribe
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
data class UserData (
    @Id
    var id:Long = 0,
    var title:String = "New User",
    var username:String = "",
    var email:String = "",
    var password:String = ""
)