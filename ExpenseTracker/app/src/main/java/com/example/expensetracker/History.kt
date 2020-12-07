package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.AdapterView
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView

//This is the class to see the whole transaction history
class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val mListView = findViewById<ListView>(R.id.listView)

        val mIntent = intent
        val mArgs = mIntent.getBundleExtra("BUNDLE")
        val obj = mArgs!!.getSerializable("LIST") as ArrayList<Transaction>

        val mAdapter = HistoryListAdapter(this, obj)
        mListView.adapter = mAdapter

        // bottom navigation
        val bottomNavPane = findViewById<BottomNavigationView>(R.id.nav_menu)
        bottomNavPane.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    //transactionHistory()
                    true
                }
                R.id.add -> {
                    goHome()
                    true
                }
                R.id.reset -> {
                    resetBudget()
                    true
                }
                else -> false
            }
        }
    }



    private fun transactionHistory() {
        val intent = Intent(this, History::class.java)
        intent.putExtra("list_of_transactions", list)
        startActivity(intent)
    }


    private fun goHome() {
        val intent = Intent(this, MainScreen::class.java)
        intent.putExtra("list_of_transactions", list)
        startActivity(intent)
    }

    private fun addTransaction() {
        val popUpDialog = TransactionDialog()
        popUpDialog.show(supportFragmentManager, "Dialog")
    }

    //Change later, skeleton for now
    private fun resetBudget() {
        val editor = sharedpreferences.edit()
        editor.putString("budget", "")
        editor.apply()
        finish()
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }



}