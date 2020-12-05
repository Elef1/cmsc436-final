package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
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

//var list = arrayListOf<Transaction>()
var list = ArrayList<Transaction>()

lateinit var mAdapter: BaseAdapter
private var transactionEntered: String? = null
private var amountEntered: String? = null
lateinit var budget: TextView
lateinit var totalBudget: TextView


class MainScreen : AppCompatActivity(), TransactionDialog.ExampleDialogListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)
        totalBudget = findViewById(R.id.totalBudgt)
        var currentBudget = intent.getStringExtra("Amount").toString()//can be used as total budget
        Log.i("TAG", "Currentbudget: $currentBudget")

        //If we enter we, we have updated current budget, hence update the previous budget and
        //clear the adapter.
        if(currentBudget != sharedpreferences.getString("totalAmount", "").toString()) {
            val editor = sharedpreferences.edit()
            editor.putString("totalAmount", currentBudget)
            editor.apply()
            Log.i("TAG", "Currentbudget: $currentBudget")
            Log.i("TAG", "sharedpref: ${sharedpreferences.getString("totalAmount", "").toString()}")
            list.clear()
            deleteHistory()

            }

        totalBudget.text = "Total budget: $currentBudget"
        //Gets budget from previous activity stored in the intent
        budget = findViewById<TextView>(R.id.yourBdgt)
        budget.text = intent.getStringExtra("Amount").toString()
        var mListView = findViewById<ListView>(R.id.listView)

        //Stops scrolling on the main screen to only see the most recent ones that were added
        mListView.isEnabled = false
        mAdapter = HistoryListAdapter(this, list)
        mListView.adapter = mAdapter

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

    //For some reason it doesn't work here. Not sure why, might relate to the list
    private fun transactionHistory() {
        val intent = Intent(this, History::class.java)
        val args = Bundle()
        args.putSerializable("LIST", list)
        intent.putExtra("BUNDLE", args)
        startActivity(intent)
    }

    private fun addTransaction() {
        val popUpDialog = TransactionDialog()
        popUpDialog.show(supportFragmentManager, "Dialog")
    }

    override fun applyTexts(transaction: String?, amount: String?) {
        var bud = Integer.parseInt(budget.text.toString())

        var amount1 = amount?.replace("\\s".toRegex(), "")

        var subtract = Integer.parseInt(amount1.toString())

        if(bud - subtract < 0) {
            openDialog2()
            Log.i("TAG", "Returned?")
            return
        }

        //warning: youre below or equal 25% of your budget
        if ((bud - subtract) <= bud/4)
            openDialog()

        Log.i("TAG", "Returned?123")

        //subtract the transaction
        bud -= subtract
        budget.text = bud.toString()


        transactionEntered = transaction.toString()
        amountEntered = amount.toString()
        Log.i("TAG", "Transaction: ${transaction.toString()} and  amount: ${amount.toString()}")
//        list.add(Transaction(transactionEntered.toString(), amountEntered.toString()))
        list.add(
            Transaction(
                "Transaction: ${transactionEntered.toString()}",
                "$ ${amountEntered.toString()}"
            )
        )
        mAdapter.notifyDataSetChanged()
    }

    //Change later, skeleton for now
    private fun resetBudget() {
        finish()
    }

    companion object {
        const val Amount = "amount"
        private const val FILE_NAME = "ExpenseTrackerData.txt"
    }

    override fun onResume() {
        super.onResume()
        if (mAdapter.count == 0)
            loadItems()
    }

    override fun onPause() {
        super.onPause()
        saveItems()
    }

    //Loads stored transactions and amounts
    private fun loadItems() {
        var reader: BufferedReader? = null
        try {
            val fis = openFileInput(FILE_NAME)
            reader = BufferedReader(InputStreamReader(fis))
            var transaction: String? = null
            var amount: String? = null

            do {
                transaction = reader.readLine();
                //If transaction is null, then amount should and will be null, hence we stop
                if (transaction == null)
                    break
                amount = reader.readLine()
                //Readd to list and update it
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

    // Save transactions and amount in a file
    private fun saveItems() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))

            for (idx in 0 until mAdapter.count) {
                var transactionInfo = mAdapter.getItem(idx) as Transaction
                writer.println(transactionInfo.transaction)
                writer.println(transactionInfo.amount)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }


    private fun deleteHistory() {
        var writer: PrintWriter? = null
        try {
            val fos = openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
            writer = PrintWriter(BufferedWriter(OutputStreamWriter(fos)))
            writer.print("")
            writer.close()

        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            writer?.close()
        }
    }

    //Warning Popup
    fun openDialog() {
        var dialog = WarningPopup()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

    //Below budget Popup
    fun openDialog2() {
        var dialog = BelowBudgetPopup()
        dialog.show(supportFragmentManager, "CustomDialog")
    }

}

