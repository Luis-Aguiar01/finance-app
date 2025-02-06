package br.edu.ifsp.dmo.financeapp.ui.activities.bills

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityFixedBillsBinding

class FixedBillsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFixedBillsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixedBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configListeners()
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }
}