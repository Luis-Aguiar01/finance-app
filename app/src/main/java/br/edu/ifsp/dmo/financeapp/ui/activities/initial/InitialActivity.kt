package br.edu.ifsp.dmo.financeapp.ui.activities.initial

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityInitialBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.login.LoginActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.main.MainActivity
import br.edu.ifsp.dmo.financeapp.ui.activities.register.RegisterActivity
import br.edu.ifsp.dmo.financeapp.util.Constants

class InitialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitialBinding
    private lateinit var registerResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var loginResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: InitialViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(InitialViewModel::class.java)

        configListeners()
        configResultLauncher()
    }

    private fun configListeners() {
        binding.loginButton.setOnClickListener {
            loginResultLauncher.launch(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        }

        binding.registerButton.setOnClickListener {
            registerResultLauncher.launch(
                Intent(
                    this,
                    RegisterActivity::class.java
                )
            )
        }

        // Caso o email salvo no dataStore não seja vazio, significa que o usuário selecionou
        // a opção de se manter logado, então o email é armazenado na view model e passado para
        // a main activity, que tem as demais funcionalidades do sistema, as quais o email do usuário
        // é necessário.
        viewModel.savedEmail.observe(this, Observer {
            if (it.isNotBlank()) {
                val resultIntent = Intent(this, MainActivity::class.java)
                resultIntent.putExtra(Constants.USER_EMAIL, it)
                setResult(RESULT_OK, resultIntent)
                startActivity(resultIntent)
                finish()
            }
        })
    }

    private fun configResultLauncher() {
        registerResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    Toast.makeText(
                        this,
                        getString(R.string.user_registered_successfuly), Toast.LENGTH_SHORT
                    ).show()
                }
            }

        loginResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    finish()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
    }
}