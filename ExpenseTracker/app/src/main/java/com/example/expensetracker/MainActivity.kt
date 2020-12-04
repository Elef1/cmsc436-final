package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

lateinit var sharedpreferences: SharedPreferences

// Main activity is the screen that opens when no amount has been entered in the app,
// hence the first screen. This can and probably will change as we continue
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var enterYourBudgetTxt: EditText? = null
        var confirmBtn: Button? = null

        //Get amount if stored in preferences
        sharedpreferences = getPreferences(Context.MODE_PRIVATE)
        val amountStored = sharedpreferences.getString(Amount, "").toString()
        //If an amount was stored, go into main screen straight away.
        //This will change later on, just a current skeleton
        if (amountStored != "") {
            val intent = Intent(this, MainScreen::class.java)
            intent.putExtra(Amount, amountStored)
            startActivity(intent)
            Log.i("TAG", "Testing1")
        } else {
            setContentView(R.layout.activity_main)
            //Get reference to btn and txt on activity_main screen
            enterYourBudgetTxt = findViewById(R.id.enterBudget)
            confirmBtn = findViewById(R.id.btnSave)
            Log.i("TAG", "Testing2")

        }
        Log.i("TAG", "Testing3")


        //ConfirmBtn on screen. Confirms amount for budget and saves it in preferences
        confirmBtn?.setOnClickListener {
            val editor = sharedpreferences.edit()
            val amount = enterYourBudgetTxt?.text.toString()
            editor.putString(Amount, amount)
            editor.apply()
            val intent = Intent(this, MainScreen::class.java)
            intent.putExtra(Amount, amount)
            startActivity(intent)
        }
    }

    companion object {
        val Amount = "amount"
    }

}