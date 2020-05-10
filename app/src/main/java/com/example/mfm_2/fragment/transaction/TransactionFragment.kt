package com.example.mfm_2.fragment.transaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        }
        fragmentTransaction.commit()

        return view
    }

}
