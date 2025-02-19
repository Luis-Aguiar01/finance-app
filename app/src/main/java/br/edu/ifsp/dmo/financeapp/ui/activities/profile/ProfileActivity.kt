package br.edu.ifsp.dmo.financeapp.ui.activities.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.edu.ifsp.dmo.financeapp.databinding.ActivityProfileBinding
import br.edu.ifsp.dmo.financeapp.ui.activities.main.MainActivity
import br.edu.ifsp.dmo.financeapp.util.Constants

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel

    companion object {
        const val RESULT_ERROR = Activity.RESULT_FIRST_USER + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        configListeners()
        configObservers()
        openBundle()
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.editButton.setOnClickListener {
            viewModel.edit()
        }

        binding.inputName.addTextChangedListener { name ->
            viewModel.setName(name.toString())
        }

        binding.inputEmail.addTextChangedListener { email ->
            viewModel.setEmail(email.toString())
        }

        binding.inputPassword.addTextChangedListener { password ->
            viewModel.setPassword(password.toString())
        }
    }

    private fun configObservers() {
        viewModel.name.observe(this, Observer { name ->
            if (binding.inputName.text.toString() != name) {
                binding.inputName.setText(name)
            }
        })

        viewModel.email.observe(this, Observer { email ->
            if (binding.inputEmail.text.toString() != email) {
                binding.inputEmail.setText(email)
            }
        })

        viewModel.password.observe(this, Observer { password ->
            if (binding.inputPassword.text.toString() != password) {
                binding.inputPassword.setText(password)
            }
        })

        viewModel.editResult.observe(this, Observer { editResult ->
            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra(Constants.USER_EMAIL, viewModel.email.value)
            if (editResult) {
                setResult(Activity.RESULT_OK, resultIntent)
            } else {
                setResult(RESULT_ERROR, resultIntent)
            }
            finish()
        })
    }

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if (email != null) {
                viewModel.updateData(email)
            }
        }
    }
}