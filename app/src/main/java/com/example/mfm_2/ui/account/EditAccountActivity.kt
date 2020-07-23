package com.example.mfm_2.ui.account

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.example.mfm_2.R
import com.example.mfm_2.model.Account
import com.example.mfm_2.viewmodel.MFMViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditAccountActivity : AppCompatActivity() {
    //    private lateinit var accountViewModel: AccountViewModel
    private lateinit var mfmViewModel: MFMViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mfmViewModel = ViewModelProvider(this).get(MFMViewModel::class.java)

        setContentView(R.layout.activity_edit_account)
        val toolbar: Toolbar = findViewById(R.id.toolbar_edit_account)
        setSupportActionBar(toolbar)
        title = "Edit Account"
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val accountName: TextInputEditText = findViewById(R.id.textedit_account_name)
        val buttonSave: Button = findViewById(R.id.button_save)

        var account: Account = Account()
        val accountId = intent.getLongExtra(EXTRA_ACCOUNT_ID, -1)
        if (accountId > 0) {
            CoroutineScope(Dispatchers.IO).launch {
                account = mfmViewModel.getAccountById(accountId)
                withContext(Dispatchers.Main) {
                    accountName.setText(account.accountName)
                }
            }
        }

        buttonSave.setOnClickListener {
            val newAccount = account
            newAccount.accountName = accountName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val resultCode = mfmViewModel.update(newAccount)
                val replyIntent = Intent()
                replyIntent.putExtra(EXTRA_UPDATE_RESULT, resultCode)
                setResult(Activity.RESULT_OK, replyIntent)
                finish()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val EXTRA_ACCOUNT_ID = "com.example.mfm_2.fragment.account.EXTRA_ACCOUNT_ID"
        const val EXTRA_UPDATE_RESULT = "com.example.mfm_2.fragment.account.EXTRA_UPDATE_RESULT"
    }
}
