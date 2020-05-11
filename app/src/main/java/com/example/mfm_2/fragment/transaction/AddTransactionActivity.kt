package com.example.mfm_2.fragment.transaction

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mfm_2.R
import com.example.mfm_2.fragment.home.HomeFragment
import com.example.mfm_2.viewmodel.AccountViewModel
import com.example.mfm_2.viewmodel.BudgetViewModel
import com.example.mfm_2.viewmodel.TransactionViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var budgetViewModel: BudgetViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        budgetViewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        setContentView(R.layout.activity_add_edit_transaction2)
        setSupportActionBar(findViewById(R.id.toolbar_add_edit_transaction))
        title = "Add Transaction"


        val viewPager2: ViewPager2 = findViewById(R.id.viewpager)
        val viewPagerAdapter = ViewPagerAdapter(this)
        viewPager2.adapter = viewPagerAdapter
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Expense"
                    tab.setIcon(R.drawable.ic_remove_circle_outline_24px)
                }
                1 -> {
                    tab.text = "Income"
                    tab.setIcon(R.drawable.ic_add_circle_outline_24px)
                }
                2 -> {
                    tab.text = "Transfer"
                    tab.setIcon(R.drawable.ic_swap_horiz_24px)
                }
            }
        }.attach()

        val buttonSave: Button = findViewById(R.id.button_save)
        val onTabSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                buttonSave.setOnClickListener {
                    when (tab.position) {
                        0 -> {
                            val fragment = viewPagerAdapter.fragmentList[tab.position] as ExpenseFragment
                            fragment.addExpense()
                            finish()
                        }
                        1 -> {
                            val fragment = viewPagerAdapter.fragmentList[tab.position] as IncomeFragment
                            fragment.addIncome()
                            finish()
                        }
                        2 -> {
                            val fragment = viewPagerAdapter.fragmentList[tab.position] as TransferFragment
                            fragment.addTransfer()
                            finish()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        }
        onTabSelectedListener.onTabSelected(tabLayout.getTabAt(0))
        tabLayout.addOnTabSelectedListener(onTabSelectedListener)
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        val fragmentList: MutableList<Fragment> = mutableListOf()

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    val fragment = ExpenseFragment()
                    fragmentList.add(fragment)
                    fragment
                }
                1 -> {
                    val fragment = IncomeFragment()
                    fragmentList.add(fragment)
                    fragment
                }
                2 -> {
                    val fragment = TransferFragment()
                    fragmentList.add(fragment)
                    fragment
                }
                else -> HomeFragment()
            }
        }
    }
}