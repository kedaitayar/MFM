package com.example.mfm_2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mfm_2.custom_class.MonthString
import com.example.mfm_2.custom_class.MonthYearPickerDialog
import com.example.mfm_2.fragment.account.AccountFragment
import com.example.mfm_2.fragment.addEditTransaction.AddEditTransactionActivity
import com.example.mfm_2.fragment.budget.BudgetFragment
import com.example.mfm_2.fragment.home.HomeFragment
import com.example.mfm_2.fragment.transaction.TransactionFragment
import com.example.mfm_2.viewmodel.AccountViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)


        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Account"
                2 -> tab.text = "Transaction"
                3 -> tab.text = "Budget"
//                4 -> tab.text = "Debt"
            }
        }.attach()

        //fab
        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditTransactionActivity::class.java)
            startActivity(intent)
        }

        //toolbar date
        val cal = Calendar.getInstance()
//        val dateLayout: TextInputLayout = findViewById(R.id.textlayout_date)
        val dateInput: TextInputEditText = findViewById(R.id.textinput_date)
        val monthShortString = MonthString().monthShortString
        dateInput.setText(monthShortString[cal.get(Calendar.MONTH)] + " " + cal.get(Calendar.YEAR))
        dateInput.setOnClickListener {
            val pickerDialog = MonthYearPickerDialog()
            pickerDialog.setListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                dateInput.setText(monthShortString[month] + " " + year)
            })
            pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
        }

        accountViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.test -> {
                val intent = Intent(this, TestActivity::class.java)
                startActivity(intent)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 4

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> HomeFragment()
                1 -> AccountFragment()
                2 -> TransactionFragment()
                3 -> BudgetFragment()
                else -> AccountFragment()
            }
        }
    }
}
