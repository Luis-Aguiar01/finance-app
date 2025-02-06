package br.edu.ifsp.dmo.financeapp.ui.activities.initial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.databinding.ActivityInitialBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.login.LoginActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.register.RegisterActivity

class InitialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
    }

    private fun configListeners() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}