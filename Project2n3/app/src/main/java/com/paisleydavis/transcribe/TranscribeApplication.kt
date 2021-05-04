package com.paisleydavis.transcribe

import android.app.Application
import android.content.Context
import android.util.Log
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.dataClasses.UserData_
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.util.*
import kotlin.math.sqrt

class TranscribeApplication: Application() {
     companion object {
         private lateinit var user: UserData
         var acceleration = 0f
         var currentAcceleration = 0f
         var lastAcceleration = 0f

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

        // implement shake to close at any point of the app using a library
        // close app on shake...I know this isn't great UX, but I wanted to try it!
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH

    }

    val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            lastAcceleration = currentAcceleration
            // feel like I could change this math so a horizontal shake is better recognized
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > .8) { // might want to be larger in real life but needs to work for emulator demo
                // logging for science
                Log.d("SHAKE", acceleration.toString())
                // close app!
                closeApp()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    fun closeApp(){
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}