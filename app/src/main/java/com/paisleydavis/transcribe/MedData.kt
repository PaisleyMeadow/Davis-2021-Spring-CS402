package com.paisleydavis.transcribe

import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class MedData (
    @Id
    var id:Long = 0,
    val name:String = "",
    val dosageAmount:Long = 0,
    val dosageUnit:String = "",

    @Convert(converter = StringListConverter::class, dbType = String::class)
    val frequencyDays:ArrayList<String>,
    val reminderOn:Boolean = false,
    val reminderHour:Int = 0, //this could probably be another type (time?)
    val reminderMinute:Int = 0
) {
    // converter class to handle frequencyDays string list
    class StringListConverter : PropertyConverter<List<String>, String> {
        override fun convertToEntityProperty(databaseValue: String?): List<String> {
            if (databaseValue == null){
                return ArrayList()
            }
            return databaseValue.split(",")
        }

        override fun convertToDatabaseValue(entityProperty: List<String>?): String {
            if (entityProperty == null) return ""
            if (entityProperty.isEmpty()) return ""
            val builder = StringBuilder()
            entityProperty.forEach { builder.append(it).append(",") }
            builder.deleteCharAt(builder.length - 1)
            return builder.toString()
        }
    }
}