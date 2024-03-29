package com.paisleydavis.transcribe

import android.app.Application
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.dataClasses.UserData_
import io.objectbox.Box
import io.objectbox.kotlin.boxFor

class TranscribeApplication: Application() {
     companion object {
         private lateinit var user: UserData

         //lazily saving a global variable for current user, used to set relations to SurveyData and MedData entries
         fun setUser(userObj: UserData){
             this.user = userObj

             // put in DB
             val userBox: Box<UserData> = ObjectBox.boxStore.boxFor()
             val userId = userBox.put(userObj)

             // get user obj from db
             this.user = userBox.query().equal(UserData_.id, userId).build().findFirst()!!
         }

         fun getUser(): UserData {
             return this.user
         }
     }

    override fun onCreate() {
        super.onCreate()
        ObjectBox.init(this)

        // initalize user TODO: could use this in staging variant
        user = UserData(username = "", password = "")
    }
}