package com.paisleydavis.transcribe

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.relation.ToOne
import java.io.Serializable
import java.util.*

@Entity
data class SurveyData(
    @Id
    var id:Long = 0,
    val topic:String = "",
    val dateOfEntry: Date? = null,
    val data: String = ""
):Serializable {
    //relation to User
    lateinit var user: ToOne<UserData>
}

