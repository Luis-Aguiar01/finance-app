package br.edu.ifsp.dmo.financeapp.ui.activities.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        configListeners()
        configObservers()
    }

    private fun configObservers() {
        viewModel.insertedUser.observe(this, Observer {
            if (it) {
                val resultIntent = Intent()
                setResult(RESULT_OK, resultIntent)
                finish()
            } else {
                Toast.makeText(this, "Não foi possível realizar o cadastro...", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener { finish() }

        binding.registerButton.setOnClickListener{

            val email =  binding.inputEmail.text.toString()
            val name =  binding.inputName.text.toString()
            val password =  binding.inputPassword.text.toString()
            val salary =  binding.inputSalary.text.toString()

            if (email.isNotBlank() && name.isNotBlank() && password.isNotBlank() && salary.isNotBlank()){
                viewModel.insertUser(name, email, password, salary.toDouble())
            } else {
                Toast.makeText(this, "Preencha todos os campos...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}