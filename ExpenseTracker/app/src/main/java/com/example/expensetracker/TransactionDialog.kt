package com.example.expensetracker

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialogFragment

class TransactionDialog : AppCompatDialogFragment() {
    private var transactionText: EditText? = null
    private var amountText: EditText? = null
    private var listener: ExampleDialogListener? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        val inflater = activity!!.layoutInflater
        val view = inflater.inflate(R.layout.dialog_view, null)
        builder.setView(view)
            .setTitle("Enter Transaction")
//            .setNegativeButton("cancel", DialogInterface.OnClickListener { dialogInterface, i -> })
//            .setPositiveButton("ok", DialogInterface.OnClickListener { dialogInterface, i ->
            .setNegativeButton("cancel", DialogInterface.OnClickListener { _, _ -> })
            .setPositiveButton("ok", DialogInterface.OnClickListener { _, _ ->
                val username = transactionText!!.text.toString()
                val password = amountText!!.text.toString()
                listener!!.applyTexts(username, password)
            })
        transactionText = view.findViewById(R.id.transaction_input)
        amountText = view.findViewById(R.id.amount_input)
        return builder.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = try {
            context as ExampleDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(context.toString() +
                    "must implement ExampleDialogListener")
        }
    }

    interface ExampleDialogListener {
        fun applyTexts(username: String?, password: String?)
    }
}
