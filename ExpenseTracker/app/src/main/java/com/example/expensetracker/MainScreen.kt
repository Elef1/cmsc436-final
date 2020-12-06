package com.example.expensetracker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.*
import java.text.ParseException

// This is the app's main screen. It shows the user their total budget, remaining budget, and
// a history of their most recent transactions (not full history).
// Here the user can enter new transactions, check their full history, and reset their budget.

val list = ArrayList<Transaction>()

class MainScreen : AppCompatActivity(), TransactionDialog.ExampleDialogListener {

    private lateinit var mAdapter: BaseAdapter
    private lateinit var budget: TextView

    companion object {
        private const val AMOUNT = "amount"
        private const val BUDGET = "budget"
        private const val FILE_NAME = "ExpenseTrackerData.txt"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        sharedpreferences = getPreferences(Context.MODE_PRIVATE)
        budget = findViewById(R.id.yourBdgt)

        val totalBudget: TextView = findViewById(R.id.totalBudgt)
        val mListView = findViewById<ListView>(R.id.listView)
        val currentBudget = intent.getStringExtra(AMOUNT).toString()

        // If the total budget has changed, update the budget and clear Transaction history.
        if (currentBudget != sharedpreferences.getString(BUDGET, "").toString()) {
            val editor = sharedpreferences.edit()
            editor.putString(BUDGET, currentBudget)
            editor.apply()
            list.clear()
            deleteHistory()
        }

        val totalBudgetStr = "Total Budget: $$currentBudget"
        totalBudget.text = totalBudgetStr
        budget.text = currentBudget

        // Stops scrolling on the home screen to only see the most recent Transactions
        // that were added.
        mListView.isEnabled = false
        mAdapter = HistoryListAdapter(this, list)
        mListView.adapter = mAdapter

        //transactionButton
        val transButton = findViewById<ImageButton>(R.id.addTrans)
        transButton.setOnClickListener {
            addTransaction()
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
                    //addTransaction()
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

        val transactionEntered: String = transaction.toString()
        val amountEntered: String = amount.toString()
        list.add(Transaction(transactionEntered, "$${amountEntered}"))
        mAdapter.notifyDataSetChanged()
    }


    private fun resetBudget() {
        val editor = sharedpreferences.edit()
        editor.putString(BUDGET, "")
        editor.apply()
        finish()
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

    // Code for save/load from Lab 4.

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

