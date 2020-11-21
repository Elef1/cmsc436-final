package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.*
import java.text.ParseException

// Add a search option to categorize things purchased(optional)
// progress bar for the budget as it goes so it doesn't look empty(at the end)
// A lot of bugs, especially when saving/loading old info. I was adjusting that and the dialog,
// but I stopped there for now. Just a skeleton to get us started before the thanksgiving break.
// Save the values placed in the dialog and save into the list(not done yet, stopped midway)
// Code for save/load I got from Lab4.

// This is the main screen after the "MainActivity" screen. Here we see the budget and
// latest transactions only, not the full history
var list = arrayListOf<Transaction>()
lateinit var mAdapter: BaseAdapter
var count = 0

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        //Gets budget from previous activity
        val budget = findViewById<TextView>(R.id.yourBdgt)
        budget.text = intent.getStringExtra(Amount).toString()
        var mListView = findViewById<ListView>(R.id.listView)

        //Stops scrolling on the main screen to only see the most recent ones that were added
        mListView.isEnabled = false
        mAdapter = HistoryListAdapter(this, list)
        mListView.adapter = mAdapter
    }

    //Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun transactionHistory() {
        val intent = Intent(this, History::class.java)
        intent.putExtra("list_of_transactions", list)
        startActivity(intent)
    }

    private fun addTransaction() {
        openDialog()
        count++
        list.add(Transaction("Food", "100"))
        mAdapter.notifyDataSetChanged()
    }

    //Change later, skeleton for now
    private fun resetBudget() {
        finish()
    }

    private fun openDialog() {
        var dialog = TransactionDialog()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    companion object {
        val Amount = "amount"
        val TAG = "Project"
        private val FILE_NAME = "ExpenseTrackerData.txt"
    }

    override fun onResume() {
        super.onResume()
        // Load saved ToDoItems, if necessary
        if (mAdapter.count == 0)
            loadItems()
    }

    override fun onPause() {
        super.onPause()
        // Save ToDoItems
        saveItems()
    }

    // Load stored ToDoItems
    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))

            var transaction: String? = null
            var amount: String? = null

            do {
                transaction = reader.readLine();
                if (transaction == null)
                    break
                amount = reader.readLine()
                //Adjust this
                list.add(Transaction(transaction, amount))
                mAdapter.notifyDataSetChanged()
            } while (true)

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: ParseException) {
            e.printStackTrace()
        } finally {
            if (null != reader) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    // Save ToDoItems to file
    private fun saveItems() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(
                BufferedWriter(
                    OutputStreamWriter(
                        fos
                    )
                )
            )
            for (idx in 0 until mAdapter.count) {
                writer.println(mAdapter.getItem(idx))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

//    fun dump() {
//        for (i in 0 until mAdapter.count) {
//            val data = (mAdapter.getItem(i) as ToDoItem).toLog()
//            Log.i(
//                TAG,
//                "Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ",")
//            )
//        }
//    }



}
