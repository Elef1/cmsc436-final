package com.example.expensetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView

//This is the class to see the whole transaction history
class History : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        var mListView = findViewById<ListView>(R.id.listView)
        var lists = intent.getSerializableExtra("list_of_transactions")
//        var lists = intent.getParcelableArrayListExtra("key");

        if(lists != null) {
           var mAdapter = HistoryListAdapter(this, lists as ArrayList<Transaction>)
            mListView.adapter = mAdapter
        }
    }

}