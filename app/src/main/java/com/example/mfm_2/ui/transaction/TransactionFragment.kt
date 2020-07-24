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
                .setMessage("TODO")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }

}
