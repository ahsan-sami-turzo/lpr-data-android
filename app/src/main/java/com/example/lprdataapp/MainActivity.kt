package com.example.lprdataapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.lprdataapp.R
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnGymData: Button = findViewById(R.id.btnLutGymData)
        btnGymData.setOnClickListener {
            thread {
                makeAPICall("https://underlying-jyoti-lut.koyeb.app/api/lut-gym-data")
            }
        }

        var btnBuffetData: Button = findViewById(R.id.btnLutBuffetData)
        btnBuffetData.setOnClickListener {
            thread {
                makeAPICall("https://underlying-jyoti-lut.koyeb.app/api/lut-buffet-data")
            }
        }
    }

    private fun makeAPICall(url: String) {

        lateinit var textView: TextView
        textView = findViewById(R.id.textView)

        val connection = URL(url).openConnection() as HttpURLConnection
        connection.connect()

        val response = connection.inputStream.bufferedReader().readText()

        // Check if the response starts with '{' or '[' to determine if it's a JSONObject or JSONArray
        if (response.trimStart().startsWith("{")) {
            // handleJSONObjectResponse(response)
            val jsonObject = JSONObject(response)

            val peopleInTheGym = jsonObject.getString("people_in_the_gym")
            val muscularConditionTraining = jsonObject.getString("muscular_condition_training")
            val aerobicTraining = jsonObject.getString("aerobic_training")
            val functionalTraining = jsonObject.getInt("functional_training")

            val formattedString = """
            People in the gym: $peopleInTheGym
            Muscular condition training: $muscularConditionTraining
            Aerobic training: $aerobicTraining
            Functional training: $functionalTraining
        """.trimIndent()

            textView.text = formattedString.toString()
        } else if (response.trimStart().startsWith("[")) {
            // handleJSONArrayResponse(response)
            val jsonArray = JSONArray(response)

            val formattedString = StringBuilder()

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getString(i)
                formattedString.appendLine(item)
            }

            textView.text = formattedString.toString()
        }
    }
}