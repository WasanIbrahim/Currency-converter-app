package com.example.currencyconverterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import org.json.JSONObject
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var spinner: Spinner
    lateinit var amountText: TextView
    lateinit var convertButton: Button
    lateinit var resultText: TextView
    lateinit var dateText: TextView

    var baseCurrency = "eur"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.currencySpinner)
        amountText = findViewById(R.id.amountText)
        convertButton = findViewById(R.id.convertButton)
        resultText = findViewById(R.id.resultText)
        dateText = findViewById(R.id.dateText)

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        //loading the spinner with the array
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cur)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = (object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                baseCurrency = parent?.getItemAtPosition(position)
                    .toString() //currency selected in the spinner
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        })
        convertButton.setOnClickListener {
            requestAPI()
        }
    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async {
                fetchData()
            }.await()

            if (data.isNotEmpty()) {
                populateRV(data)
            } else {
                Log.d("MAiN", "Unable to get data")
            }
        }
    }

    private fun fetchData(): String {
        var response = ""
        try {
            response =
                URL("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/eur.json").readText()
        } catch (e: Exception) {
            Log.d("MAIN", "ISSUE")
        }
        return response
    }

    private suspend fun populateRV(data: String) {
        withContext(Main) {

            val jsonObject = JSONObject(data) //all data including date
            val currencies = jsonObject.getJSONObject("eur")//all data including date
            val date = jsonObject.getString("date")//date


            val myCurrency = currencies.getString("inr") //each currency value
            val myCurrency2 = currencies.getString("usd")
            val myCurrency3 = currencies.getString("aud")
            val myCurrency4 = currencies.getString("sar")
            val myCurrency5 = currencies.getString("cny")
            val myCurrency6 = currencies.getString("jpy")

            //converting text view to int
            var theAmountText = amountText.text.toString().toDouble()

            dateText.text = date.toString()


//            //checking if the spinner value same is the string
            if (baseCurrency == "inr") {
                resultText.text = ((myCurrency.toDouble() * theAmountText).toString())
            } else if (baseCurrency == "usd") {
                resultText.text = ((myCurrency2.toDouble() * theAmountText).toString())
            } else if (baseCurrency == "aud") {
                resultText.text = ((myCurrency3.toDouble() * theAmountText).toString())
            } else if (baseCurrency == "sar") {
                resultText.text = ((myCurrency4.toDouble() * theAmountText).toString())
            } else if (baseCurrency == "cny") {
                resultText.text = ((myCurrency5.toDouble() * theAmountText).toString())
            } else if (baseCurrency == "jpy") {
                resultText.text = ((myCurrency6.toDouble() * theAmountText).toString())
            }
        }
    }
}