package com.example.mfm_2.fragment.budget

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.mfm_2.R
import com.example.mfm_2.fragment.budget.adapter.BudgetListAdapter
import com.example.mfm_2.model.Budget
import com.example.mfm_2.viewmodel.BudgetViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * A simple [Fragment] subclass.
 */
class BudgetFragment : Fragment() {
    private lateinit var budgetViewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_budget, container, false)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerview_budget)
        val budgetAdapter = BudgetListAdapter(this.context!!)
        recyclerView.adapter = budgetAdapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        budgetViewModel.allBudget.observe(viewLifecycleOwner, Observer {budget ->
            budget?.let { budgetAdapter.setData(it) }
        })

        val addBudget: Button = view.findViewById(R.id.button_add_budget)

        addBudget.setOnClickListener {
            val budget = Budget(budgetName = "New Budget", budgetGoal = 0.0, budgetAllocation = 0.0)
            CoroutineScope(IO).launch {
                val test = budgetViewModel.insertWithResult(budget)
                withContext(Main){
                    if (test == -1L){
                        Toast.makeText(this@BudgetFragment.context, "Budget already exist", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@BudgetFragment.context, "New budget inserted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        (recyclerView.itemAnimator as SimpleItemAnimator?)!!.supportsChangeAnimations = false

//        budgetAdapter.setOnItemClickListener(object : BudgetListAdapter.OnItemClickListener{
//            override fun onItemClick(budget: Budget, view: View) {
////                Toast.makeText(this@BudgetFragment.context, "haha", Toast.LENGTH_SHORT).show()
//                val expandable: ConstraintLayout = view.findViewById(R.id.constraint_layout_expandable_detail)
//                if (expandable.visibility == View.GONE){
//                    expandable.visibility = View.VISIBLE
//                    expandable.animate().translationY(0f)//.alpha(1f)
//                } else {
//                    expandable.visibility = View.GONE
//                    expandable.animate().translationY(0f)//.alpha(0f)
//                }
//            }
//        })

        return view
    }

}
