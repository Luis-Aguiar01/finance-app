package br.edu.ifsp.dmo.financeapp.ui.activities.purchase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityAddPurchaseBinding

class AddPurchaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPurchaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }
}