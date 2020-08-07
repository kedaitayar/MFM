package com.example.mfm_2.ui.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager

import com.example.mfm_2.R

/**
 * A simple [Fragment] subclass.
 */
class TransactionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)
        val fragmentManager: FragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction().apply {
            replace(R.id.fragment_transaction_container1, TransactionListFragment())
            replace(R.id.fragment_transaction_container2, TransactionGraphFragment())
        }
        fragmentTransaction.commit()

        val info: Button = view.findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Transaction")
                .setMessage("This section is use to manage and track your transaction. Tap the floating action button at the bottom right to add new transaction. Tap the overflow menu(3 dot) to view more option.\n\nThere are three type of transaction.\nExpense is a transaction where you use your money to buy something.\nIncome is a transaction where you receive money.\nTransfer is where you transfer money between 2 account.")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        view.findViewById<Button>(R.id.info_button2).setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Transaction Flow")
                .setMessage("This card show a graph of total transaction amount of each weeks. This graph is useful to see your spending trend.")
                .setPositiveButton("OK"){ dialog, which -> }
            builder.create().show()
        }

        view.findViewById<Button>(R.id.info_button3).setOnClickListener {
            val builder = AlertDialog.Builder(this.context!!)
            builder.setTitle("Transaction List")
                .setMessage("This card show list of all transaction.")
                .setPositiveButton("OK"){ dialog, which -> }
            builder.create().show()
        }

        return view
    }

}
