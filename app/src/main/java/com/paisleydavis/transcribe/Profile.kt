package com.paisleydavis.transcribe

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.paisleydavis.transcribe.dataClasses.UserData
import com.paisleydavis.transcribe.fragments.BottomBarFragment
import com.paisleydavis.transcribe.fragments.MedContainerFragment
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

//TODO: Optimize activity order -- make Profile top stack when called; bug when going back to login page and then back to profile
class Profile : AppCompatActivity() {

    // for saving profile photo
    lateinit var currPhotoPath: String
    val REQUEST_IMAGE_CAPTURE = 1
    val REQUEST_PHOTO_GALLERY = 0

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
        val usernameText = findViewById<TextView>(R.id.username)
        if (usernameStr != null) {
            usernameText.text = "$usernameStr."
        }
        else if(TranscribeApplication.getUser().username != ""){
            usernameText.text = TranscribeApplication.getUser().username
        }
        //arrays for day and month names
        val dayNames = arrayOf(
            "Sunday",
            "Monday",
            "Tuesday",
            "Wednesday",
            "Thursday",
            "Friday",
            "Saturday"
        )
        val monthNames = arrayOf(
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
        )

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
                .add(R.id.medFragmentContainer, medContainerFragment, "MED_CONTAINER")
                .commit()
        }

        // if saved photo exists, set as profile image
        if(TranscribeApplication.getUser().photoPath != ""){
            currPhotoPath = TranscribeApplication.getUser().photoPath
            setPic()
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

        // When profile photo is tapped and held, take new profile photo and replaces current one
        // gonna be honest, this is mostly Ziray's code, but like, how else would I do it?
        // photo taken is now saved in files and path is saved w/ in UserData db
        profileImage.setOnLongClickListener{
            showPhotoDialog()
            true
        }
    }

    private fun askCameraPermission(){
        val cameraCheckPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        )

        if (cameraCheckPermission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.CAMERA
                    )){
                val builder = AlertDialog.Builder(this)

                val message = getString(R.string.permission_message)
                builder.setTitle(R.string.permission_title)
                        .setMessage(message)
                        .setPositiveButton("OK") { _, _ ->
                            requestPermission("camera")
                        }
                val dialog = builder.create()
                dialog.show()
            }
            else{
                //don't display permission request
                requestPermission("camera")
            }
        }
        else{
            launchCamera()
        }
    }


    private fun askGalleryPermission() {
        val readExternalCheckPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (readExternalCheckPermission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    )){
                val builder = AlertDialog.Builder(this)

                val message = getString(R.string.gallery_permission_message)
                builder.setTitle(R.string.gallery_permission_title)
                        .setMessage(message)
                        .setPositiveButton("OK") { _, _ ->
                            requestPermission("gallery")
                        }
                val dialog = builder.create()
                dialog.show()
            }
            else{
                //don't display permission request
                requestPermission("gallery")
            }
        }
        else{
            goToGallery()
        }
    }

    // if a new intent has been sent, it means a med fragment needs to be deleted
    // so we save the new intent so it can be accessed in MedContainerFragment
    override fun onNewIntent(intent: Intent){
        super.onNewIntent(intent)
        this.intent = intent
    }

    private fun requestPermission(type: String) {
        if(type == "camera") {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 123)
        }
        else if(type == "gallery"){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for ((index, permission) in permissions.withIndex()){
            if( permission == Manifest.permission.CAMERA){
                if( grantResults[index] == PackageManager.PERMISSION_GRANTED){
                    launchCamera()
                }
            }
            else if(permission == Manifest.permission.READ_EXTERNAL_STORAGE){
                if( grantResults[index] == PackageManager.PERMISSION_GRANTED){
                    goToGallery()
                }
            }
        }
    }

    // asks if user wants to choose from gallery or take new picture
    // and calls methods to handle permissions for each choice
    private fun showPhotoDialog() {
        val builder = AlertDialog.Builder(this@Profile)
        builder.apply{
            setPositiveButton("Take Photo",
                    DialogInterface.OnClickListener{ dialog, id ->
                        askCameraPermission()
                    })
            setNegativeButton("Choose From Gallery",
                    DialogInterface.OnClickListener{ dialog, id ->
                        Log.d("GALLERY", "gallerryy")
                        askGalleryPermission()
//                        goToGallery()
                    })
        }
        builder.setMessage("Change your profile photo?")
        builder.setCancelable(true)

        val alert = builder.create()
        alert.show()
    }

    // access photo in gallery
    private fun goToGallery(){
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, REQUEST_PHOTO_GALLERY)
    }

    // photo is now saved to files and path is saved in db
    // launchCamera is called when the user taps and holds the photo and accepts the permissions
    // yeee I mostly copied Android doc code for this but ayyy it works
    private fun launchCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { cameraIntent ->
            try{
                val photoFile: File? = try {
                    createImageFile()

                } catch (ex: IOException) {
                    // Error occurred while creating the File
                    Log.d("ERROR", "Error with creating photo file.")
                    null
                }
                if(photoFile != null){
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.paisleydavis.transcribe.fileprovider",
                        photoFile
                    )

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE)
                }
            } catch(ex: ActivityNotFoundException){
                Log.d("ERROR", "cameraIntent activity not found.")
            }
        }
    }

    // creates file path for saving photo
    // XML for this
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currPhotoPath = absolutePath
        }
    }

    // called when photo is taken and returns to activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // if new picture was taken
        if( requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            galleryAddPic()
            setPic()

            // add photo path to db
            addToDB()
        } // if photo was chosen from gallery
        else if(requestCode == REQUEST_PHOTO_GALLERY && resultCode == RESULT_OK){
            val selectedImage: Uri? = data?.data
            // set image path to be saved in db
            if(selectedImage != null){
                currPhotoPath = getPathFromURI(selectedImage)
            }
            setPic()
            addToDB()
        }
    }

    fun getPathFromURI(ContentUri: Uri?): String {
        var res: String = ""
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentResolver
                .query(ContentUri!!, proj, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            cursor.close()
        }
        return res
    }

    // saves path to photo in user db
    private fun addToDB() {
        val userBox = ObjectBox.boxStore.boxFor(UserData::class.java)
        val user = TranscribeApplication.getUser()
        user.photoPath = currPhotoPath
        userBox.put(user)
    }

    // save photo to gallery
    private fun galleryAddPic() {
        // love how their own docs uses a deprecated method
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    // convert photo from gallery to bitmap and set as profile photo
    private fun setPic() {
        if(currPhotoPath != "") {
            val imageView = findViewById<ImageView>(R.id.profileImage)

            // Get the dimensions of the View
            // hardcoding 100 x 100 dimensions because getting height/width of ImageVIew returns 0 in onCreate()
            val targetW = 100
            val targetH = 100

            val bmOptions = BitmapFactory.Options().apply {
                // Get the dimensions of the bitmap
                inJustDecodeBounds = true

                BitmapFactory.decodeFile(currPhotoPath, this)

                val photoW: Int = outWidth
                val photoH: Int = outHeight

                // Determine how much to scale down the image
                val scaleFactor: Int =
                        1.coerceAtLeast((photoW / targetW).coerceAtMost(photoH / targetH))

                // Decode the image file into a Bitmap sized to fill the View
                inJustDecodeBounds = false
                inSampleSize = scaleFactor
            }
            BitmapFactory.decodeFile(currPhotoPath, bmOptions)?.also { bitmap ->
                imageView.setImageBitmap(bitmap)
            }
        }
    }
}

