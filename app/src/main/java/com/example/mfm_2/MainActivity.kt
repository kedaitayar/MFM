package com.example.mfm_2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.mfm_2.util.MonthYearPickerDialog
import com.example.mfm_2.ui.NotBudgetedFragment
import com.example.mfm_2.ui.account.AccountFragment
import com.example.mfm_2.ui.budget.BudgetFragment
import com.example.mfm_2.ui.transaction.TransactionFragment
import com.example.mfm_2.singleton.SelectedDateSingleton
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)

        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        val viewPager: ViewPager2 = findViewById(R.id.viewpager)
        viewPager.adapter = ViewPagerAdapter(this)

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Account"
                1 -> tab.text = "Transaction"
                2 -> tab.text = "Budget"
            }
        }.attach()

        val fragmentNotBudget = supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_not_budgeted_container, NotBudgetedFragment())
        }
        fragmentNotBudget.commit()

        //fab
        val fab: FloatingActionButton = findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, com.example.mfm_2.ui.transaction.AddTransactionActivity::class.java)
            startActivity(intent)
        }

        //toolbar date
        val dateInput: TextInputEditText = findViewById(R.id.textinput_date)
        val monthShortString = MonthYearPickerDialog().months
        dateInput.setText(monthShortString[(SelectedDateSingleton.singletonSelectedDate.month - 1)] + " " + SelectedDateSingleton.singletonSelectedDate.year)
        dateInput.setOnClickListener {
            val selectedDate = mfmViewModel.selectedDate.value
            selectedDate?.let {
                val pickerDialog = MonthYearPickerDialog(it.month, it.year)
                pickerDialog.setListener(DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    mfmViewModel.setSelectedDate(month, year)
                })
                pickerDialog.show(supportFragmentManager, "MonthYearPickerDialog")
            }
        }
        mfmViewModel.selectedDate.observe(this, Observer {
            it?.let {
                dateInput.setText("${monthShortString[it.month - 1]} ${it.year}")
            }
        })

        val info: Button = findViewById(R.id.info_button)
        info.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Date")
                .setMessage("This toolbar display the selected date. The selected date will be mostly used in the budget section and account section.")
                .setPositiveButton("OK"){ dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }
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
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> AccountFragment()
                1 -> TransactionFragment()
                2 -> BudgetFragment()
                else -> AccountFragment()
            }
        }
    }
}
