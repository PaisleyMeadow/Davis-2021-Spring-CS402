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
 * Where "+" add medication button on Profile activity will take you to
 */
class AddMedActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_med)

        val nameTextView = findViewById<TextView>(R.id.addMedName)
        val dosageTextView = findViewById<TextView>(R.id.addMedDosage)

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

//        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
//        }
    }
}