package br.edu.ifsp.dmo.financeapp.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityAddPurchaseBinding
import br.edu.ifsp.dmo.financeapp.databinding.ActivityMainBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.bills.FixedBillsActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.chart.ChartActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.goals.GoalsActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.historical.HistoricalActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.profile.ProfileActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.purchase.AddPurchaseActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCardClick()
    }

    private fun setCardClick() {
        binding.buyCard.setOnClickListener {
            startActivity(Intent(this, AddPurchaseActivity::class.java))
        }

        binding.checkExpensesCard.setOnClickListener {
            startActivity(Intent(this, ChartActivity::class.java))
        }

        binding.financialGoalsCard.setOnClickListener {
            startActivity(Intent(this, GoalsActivity::class.java))
        }

        binding.fixedBillsCard.setOnClickListener() {
            startActivity(Intent(this, FixedBillsActivity::class.java))
        }

        binding.historicalCard.setOnClickListener {
            startActivity(Intent(this, HistoricalActivity::class.java))
        }

        binding.profileCard.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.logout.setOnClickListener {
            finish()
        }
    }
}