package com.example.expensetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ListView
import com.google.android.material.bottomnavigation.BottomNavigationView

//This is the class to see the whole transaction history
class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        var mListView = findViewById<ListView>(R.id.listView)

        val mIntent = intent
        val mArgs = mIntent.getBundleExtra("BUNDLE")
        val obj = mArgs!!.getSerializable("LIST") as ArrayList<Transaction>

        if(obj != null) {
            var mAdapter = HistoryListAdapter(this, obj)
            mListView.adapter = mAdapter
        }

        // bottom navigation
        val bottomNavPane = findViewById<BottomNavigationView>(R.id.nav_menu)
        bottomNavPane.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.history -> {
                    transactionHistory()
                    true
                }
                R.id.add -> {
                    addTransaction()
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

    private fun addTransaction() {
        val popUpDialog = TransactionDialog()
        popUpDialog.show(supportFragmentManager, "Dialog")
    }

    //Change later, skeleton for now
    private fun resetBudget() {
        finish()
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }



}