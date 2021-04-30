package com.paisleydavis.transcribe

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.paisleydavis.transcribe.fragments.BottomBarFragment
import com.paisleydavis.transcribe.fragments.MedContainerFragment
import io.objectbox.Box
import java.util.*

//TODO: Optimize activity order -- make Profile top stack when called; bug when going back to login page and then back to profile
class Profile : AppCompatActivity() {

    //get medication container fragment
    private val medContainerFragment = MedContainerFragment.newInstance("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        //initialize bottom bar fragment
        val bottomBar = BottomBarFragment.newInstance("", "")
        supportFragmentManager.beginTransaction()
            .replace(R.id.profile_bottom_bar, bottomBar)
            .commit();

        val usernameStr = intent.getStringExtra("username")

        //display username in activity
        Log.d("NAME", usernameStr.toString())
        val usernameText = findViewById<TextView>(R.id.username)
        if (usernameStr != null) {
            usernameText.text = "$usernameStr."
        }
        //arrays for day and month names
        val dayNames = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val monthNames = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")

        //get current date
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = monthNames[c.get(Calendar.MONTH)]
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dayName = dayNames[c.get(Calendar.DAY_OF_WEEK) - 1]
        //set date
        val dateText = findViewById<TextView>(R.id.profileDate)
        val dateStr = "$dayName, $month $day, $year."
        dateText.text = dateStr


        //place med container fragment into the profile container
        if(savedInstanceState == null){
            supportFragmentManager.beginTransaction()
                .add(R.id.medFragmentContainer, medContainerFragment)
                .commit()
        }

        //when photo is tapped, display an instructional message on how to change it
        val profileImage = findViewById<ImageView>(R.id.profileImage)
        val profileImageText = findViewById<TextView>(R.id.profileImageText)
        profileImage.setOnClickListener{

            if(profileImageText.visibility == View.GONE){
                profileImageText.visibility = View.VISIBLE
                profileImage.alpha = 0.4f

                //wait 1 second before photo goes back to normal
                Handler().postDelayed({
                    profileImageText.visibility = View.GONE
                    profileImage.alpha = 1f
                }, 1000)
            }
        }

        //When profile photo is tapped and held, take new profile photo and replaces current one
        //gonna be honest, this is mostly Ziray's code, but like, how else would I do it?
        profileImage.setOnLongClickListener{
            val cameraCheckPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

            if (cameraCheckPermission != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)){
                    val builder = AlertDialog.Builder(this)

                    val message = getString(R.string.permission_message)
                    builder.setTitle(R.string.permission_title)
                            .setMessage(message)
                            .setPositiveButton("OK") { _, _ ->
                                requestPermission()
                            }

                    val dialog = builder.create()
                    dialog.show()
                }
                else{
                    //don't display permission request
                    requestPermission()
                }
            }
            else{
                launchCamera()
            }
            //setOnLongClickListener needs to return a boolean to notify if "you have actually consumed the event"
            true
        }
    } /////

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, 9090)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for ((index, permission) in permissions.withIndex()){
            if( permission == Manifest.permission.CAMERA){
                if( grantResults[index] == PackageManager.PERMISSION_GRANTED){
                    launchCamera()
                }
            }
        }
    }

    //TODO: actually save photo instead of just using bitmap data?
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == 9090){

            if( data != null ) {
                val imageData: Bitmap = data.extras!!.get("data") as Bitmap

                val imageView = findViewById<ImageView>(R.id.profileImage)
                imageView.setImageBitmap(imageData)
            }
        }
    }
}

