package br.edu.ifsp.dmo.financeapp.ui.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }
    }
}