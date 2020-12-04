package com.example.expensetracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlin.collections.ArrayList

//adapter class for the history list
class HistoryListAdapter(context: Context, listTransactions: ArrayList<Transaction>): BaseAdapter() {

    private val mContext: Context = context
    private val list = listTransactions

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
        val rowView = layoutInflater.inflate(R.layout.adapter_view_layout, parent, false)

        //sets up the name(transaction)
        val nameOfTransaction = rowView.findViewById<TextView>(R.id.textView1)
        nameOfTransaction.text = list[position].transaction
        //sets up the amount
        val amountOfTransaction = rowView.findViewById<TextView>(R.id.textView2)
        amountOfTransaction.text = list[position].amount
        //returns how each rows view will be
        return rowView
    }

}