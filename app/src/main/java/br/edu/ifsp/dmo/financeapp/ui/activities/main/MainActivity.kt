package br.edu.ifsp.dmo.financeapp.ui.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.chart.ChartActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.goals.GoalsActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.historical.HistoricalActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.initial.InitialActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.profile.ProfileActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.purchase.AddPurchaseActivity
import br.edu.ifsp.dmo.financeapp.util.Constants

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var profileResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        profileResultLauncher = configProfileResultLauncher()

        openBundle()
        setCardClick()
        configObserver()
    }

    private fun configObserver() {
        viewModel.isDisconnected.observe(this, Observer { isDisconnected ->
            if (isDisconnected) {
                startActivity(Intent(this, InitialActivity::class.java))
                finish()
            }
        })

        viewModel.user.observe(this, Observer {
            if (it != null) {
                binding.viewTitle.text = getString(R.string.welcome_message, it.name)
            }
        })
    }

    private fun setCardClick() {
        binding.buyCard.setOnClickListener {
            val buyIntent = Intent(this, AddPurchaseActivity::class.java)
            redirect(buyIntent)
        }

        binding.financialGoalsCard.setOnClickListener {
            val financialIntent = Intent(this, GoalsActivity::class.java)
            redirect(financialIntent)
        }

        binding.historicalCard.setOnClickListener {
            val historicalIntent = Intent(this, HistoricalActivity::class.java)
            redirect(historicalIntent)
        }

        binding.profileCard.setOnClickListener {
            val profileIntent = Intent(this, ProfileActivity::class.java)
            profileIntent.putExtra(Constants.USER_EMAIL, viewModel.getEmail())
            profileResultLauncher.launch(profileIntent)
        }

        binding.checkExpensesCard.setOnClickListener {
            val chartIntent = Intent(this, ChartActivity::class.java)
            redirect(chartIntent)
        }

        binding.logout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if (email != null) {
                viewModel.setEmail(email)
            }
        }
    }

    private fun redirect(intent: Intent) {
        intent.putExtra(Constants.USER_EMAIL, viewModel.getEmail())
        startActivity(intent)
    }

    private fun configProfileResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val extras = result.data?.extras
                    val email = extras?.getString(Constants.USER_EMAIL)
                    if (email != null) {
                        viewModel.setEmail(email)
                        Toast.makeText(
                            this,
                            getString(R.string.profile_updated_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                ProfileActivity.RESULT_ERROR -> {
                    Toast.makeText(
                        this,
                        getString(R.string.email_already_registered_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}