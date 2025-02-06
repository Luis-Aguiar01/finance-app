package br.edu.ifsp.dmo.financeapp.ui.activities.historical

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityHistoricalBinding

class HistoricalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoricalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoricalBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configListeners()
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }
}