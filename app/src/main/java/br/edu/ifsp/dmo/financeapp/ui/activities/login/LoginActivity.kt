package br.edu.ifsp.dmo.financeapp.ui.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityLoginBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.main.MainActivity
import br.edu.ifsp.dmo.financeapp.util.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        configListeners()
        configObservers()
    }

    private fun configObservers() {
        viewModel.isLogged.observe(this, Observer { isLoginCorrect ->
            if (isLoginCorrect) {
                navigateToLogged()
            } else {
                Toast.makeText(this, getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.loginPreferences.observe(this, Observer {
            val (saveLogin, stayLogged) = it
            binding.checkboxSaveData.isChecked = saveLogin
            binding.checkboxStayLogged.isChecked = stayLogged
        })

        viewModel.dataPreferences.observe(this, Observer {
            val (email, password) = it
            binding.inputEmail.setText(email)
            binding.inputPassword.setText(password)
        })
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()
            val stayLogged = binding.checkboxStayLogged.isChecked
            val saveData = binding.checkboxSaveData.isChecked

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, getString(R.string.error_empty_values), Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.login(email, password, stayLogged, saveData)
            }
        }
    }

    private fun navigateToLogged() {
        val resultIntent = Intent(this, MainActivity::class.java)
        resultIntent.putExtra(Constants.USER_EMAIL, binding.inputEmail.text.toString())
        setResult(RESULT_OK, resultIntent)
        startActivity(resultIntent)
        finish()
    }
}