package com.paisleydavis.transcribe

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.util.Log
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Where "+" add medication button on Profile activity takes you to.
 * A form where user inputs info about a new med/supplement, which will then return to the Profile
 * activity, and notify MedContainerFragment to create a new MedFragment
 */
class AddMedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med)

        val nameTextView = findViewById<TextView>(R.id.addMedName)
        val dosageTextView = findViewById<TextView>(R.id.addMedDosage)

        /**
         * Currently, this is a poor implementation of the observer pattern, as it is only a 1 to 1
         * relationship (adding med activity notifies this fragment to create a new medfragment.
         * However, when we get to databases, the med activity can also be used to add that data
         * to the database, which (I think) will make for a more appropiate application of this
         * pattern.
         */
        val addButton = findViewById<Button>(R.id.addNewMedButton)
        addButton.setOnClickListener{
            val nameText = nameTextView.text.toString()
            val dosageText = dosageTextView.text.toString()

            if(nameText != "" && dosageText != ""){
                //trigger event bus observer in MedContainerFragment
                EventBus.getDefault().post(NewMedEvent(nameText, dosageText))

                //finish this activity
                finish()
            }
            else{

                var errMessage = ""
                if(nameText == ""){
                    errMessage = "Please enter a medication or supplement name."
                }
                else if(dosageText == ""){
                    errMessage = "Please enter a dosage for this medication or supplement."
                }

                Snackbar.make(
                        findViewById(R.id.OuterAddMedLayout),
                        errMessage,
                        Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}