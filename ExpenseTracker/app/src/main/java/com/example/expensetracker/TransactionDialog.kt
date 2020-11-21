package com.example.expensetracker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

class TransactionDialog: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        var rootView: View = inflater.inflate(R.layout.dialog_view, container, false)
//        return rootView
        return inflater.inflate(R.layout.dialog_view, container, false)
    }

}
