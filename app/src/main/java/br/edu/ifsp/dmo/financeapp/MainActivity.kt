package br.edu.ifsp.dmo.financeapp

import android.os.Bundle
import android.text.Layout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityMainBinding

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
            Toast.makeText(this, "Buy Card", Toast.LENGTH_SHORT).show()
        }

        binding.checkExpensesCard.setOnClickListener {
            Toast.makeText(this, "Check Expenses", Toast.LENGTH_SHORT).show()
        }

        binding.financialGoalsCard.setOnClickListener {
            Toast.makeText(this, "Financial Goals Card", Toast.LENGTH_SHORT).show()
        }

        binding.fixedBillsCard.setOnClickListener() {
            Toast.makeText(this, "Fixed Bills Card", Toast.LENGTH_SHORT).show()
        }
    }

}