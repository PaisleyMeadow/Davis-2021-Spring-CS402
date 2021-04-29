package com.paisleydavis.transcribe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.quickbirdstudios.surveykit.*
import com.quickbirdstudios.surveykit.result.StepResult
import com.quickbirdstudios.surveykit.result.TaskResult
import com.quickbirdstudios.surveykit.steps.CompletionStep
import com.quickbirdstudios.surveykit.steps.InstructionStep
import com.quickbirdstudios.surveykit.steps.QuestionStep
import com.quickbirdstudios.surveykit.steps.Step
import com.quickbirdstudios.surveykit.survey.SurveyView
import io.objectbox.Box
import io.objectbox.kotlin.boxFor
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class Contribute : AppCompatActivity() {

    private lateinit var surveyView: SurveyView
    private lateinit var container: ViewGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contribute)

        //begin implementing survey per instructions in quickbird github readme
        surveyView = findViewById(R.id.contribute_survey)
        container = findViewById(R.id.survey_container)
        setupSurvey(surveyView)
    }

    // TODO: wanted to make it so this function was more generic and parsed a JSON file to create questions for any topic
    private fun setupSurvey(surveyView: SurveyView?) {

        val steps = listOf(
            InstructionStep(
                title = "Testosterone",
                text = "Answer the following questions to help contribute to the community knowledge base.",
                buttonText = "Continue"
            ),
            QuestionStep(
                title =  "A bit about you...",
                text = "How old are you?",
                answerFormat = AnswerFormat.IntegerAnswerFormat(
                )
            ),
            QuestionStep(
                title = "A bit about you...",
                text = "Are you a...",
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice("Man"),
                        TextChoice("Woman"),
                        TextChoice("Non-binary Person"),
                        TextChoice("Other")
                    )
                )
            ),
            QuestionStep(
                title = "Within or during the 1st month of starting testosterone treatments: ",
                text = "Did you experience weight gain?",
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice("Yes"),
                        TextChoice("No"),
                    )
                )
            ),
            QuestionStep(
                title = "Within or during 6 months of starting testosterone treatments: ",
                text = "Did you experience weight gain?",
                answerFormat = AnswerFormat.SingleChoiceAnswerFormat(
                    textChoices = listOf(
                        TextChoice("Yes"),
                        TextChoice("No"),
                    )
                )
            ),
            CompletionStep(
                title = "Thank you.",
                text = "Your answers will help other members of the community",
                buttonText = "Finish"
            )
        )

        // order questions in survey (NavigableOrderedTask just presents in order given)
        val task = NavigableOrderedTask(steps = steps)

        // can create special rules for navigation after specific questions
//        task.setNavigationRule(
//            steps[2].id,
//            NavigationRule.ConditionalDirectionStepNavigationRule(
//                resultToStepIdentifierMapper = { input ->
//                    when (input){
//                        "Yes" -> steps[2].id
//                    }
//                }
//            )
//        )



        // callback when survey is finished
        surveyView?.onSurveyFinish = { taskResult: TaskResult, reason: FinishReason ->
            if (reason == FinishReason.Completed){
//                taskResult.results.forEach { stepResult ->
//                    Log.e("RESULTS", "${stepResult.results.firstOrNull()}")
//                }

                // TODO: extrapolate adding answers to db w/ eventbus
                addToDb(taskResult.results)
                returnToCommunity()
            }
            else{
                returnToCommunity()
            }
        }

        // can configure survey look, but this is just default for now
        val configuration = SurveyTheme(
            themeColorDark = ContextCompat.getColor(this, R.color.almost_black),
            themeColor = ContextCompat.getColor(this, R.color.main_purple),
            textColor = ContextCompat.getColor(this, R.color.main_purple),
        )

        surveyView?.start(task, configuration)
    }

    private fun addToDb(results: List<StepResult>) {

        // being lazy right now, creating an array of answers to store as a JSON
        val arr = arrayListOf<String>()
        for(q in results){
            arr.add(q.toString())
        }
        val str = Gson().toJson(arr)

        //get box for survey data
        val surveyBox: Box<SurveyData> = ObjectBox.boxStore.boxFor()
        val newSurveyData = SurveyData(topic = "Testosterone", dateOfEntry = Date(), data = str)
        newSurveyData.user.target = TranscribeApplication.getUser()
        surveyBox.put(newSurveyData)

        val res = surveyBox.all
        Log.d("SURVEY", res.toString())
    }

    private fun returnToCommunity() {
        // go back to community activity when survey is finished
        val intent = Intent(this, Community::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
        startActivity(intent)
        finish()
    }

    //TODO: ask about best way to read file and process JSON?
    private fun loadJSON(filename: String): String{
        val json: String?
        try {
            val inputStream = assets.open("TestosteroneQuestions.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            val charset: Charset = Charsets.UTF_8
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset)
        }
        catch(ex: IOException){
            Log.e("ERR", "Error reading JSON: " + ex.printStackTrace().toString())
            return ""
        }
        return json
    }
}