package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

// This is the activity that opens when no budget has been entered in the app yet.
// It is the first screen a new user sees.
// This is also the screen that opens whenever the user resets their budget.

lateinit var sharedpreferences: SharedPreferences

class MainActivity : AppCompatActivity() {

    companion object {
        private const val AMOUNT = "amount"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val enterYourBudgetTxt: EditText = findViewById(R.id.enterBudget)
        val confirmBtn: Button = findViewById(R.id.btnSave)

        // Get the budget amount if it is stored in preferences.
        sharedpreferences = getPreferences(Context.MODE_PRIVATE)
        val amountStored = sharedpreferences.getString(AMOUNT, "").toString()

        // If an amount was stored, go to the home screen right away.
        if (amountStored != "") {
            val intent = Intent(this, MainScreen::class.java)
            intent.putExtra(AMOUNT, amountStored)
            startActivity(intent)
        }

        // Confirms amount for budget, saves it to preferences, and goes to home screen.
        confirmBtn.setOnClickListener {
            val editor = sharedpreferences.edit()
            val amount = enterYourBudgetTxt.text.toString()

            // Validates that the user enters a number.
            val reg = Regex("^\\d+(\\.\\d+)?$")

            if (amount.isNullOrEmpty() || !reg.matches(amount)) {
                Toast.makeText(
                    applicationContext,
                    "Please enter a number to continue!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                editor.putString(AMOUNT, amount)
                editor.apply()
                val intent = Intent(this, MainScreen::class.java)
                intent.putExtra(AMOUNT, amount)
                startActivity(intent)
            }
        }
    }
}