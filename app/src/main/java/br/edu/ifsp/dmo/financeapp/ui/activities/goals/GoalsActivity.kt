package br.edu.ifsp.dmo.financeapp.ui.activities.goals

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityGoalsBinding
import br.edu.ifsp.dmo.financeapp.databinding.LayoutDialogGoalBinding
import br.edu.ifsp.dmo.financeapp.ui.adapter.goal.GoalAdapter
import br.edu.ifsp.dmo.financeapp.ui.listeners.goal.GoalItemClickListener
import br.edu.ifsp.dmo.financeapp.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GoalsActivity : AppCompatActivity(), GoalItemClickListener {

    private lateinit var binding: ActivityGoalsBinding
    private lateinit var viewModel: GoalViewModel
    private val adapter = GoalAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(GoalViewModel::class.java)

        openBundle()
        configListeners()
        configObservers()
        configRecyclerView()
    }

    private fun configObservers(){
        viewModel.inserted.observe(this, Observer {
            if(it){
                Toast.makeText(this, "Produto adicionado com sucesso.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.goals.observe(this, Observer {
            adapter.submitDatabase(it)
        })
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.addButton.setOnClickListener {
            val dialogBinding = LayoutDialogGoalBinding.inflate(layoutInflater)

            val dialog = MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                .setView(dialogBinding.root)
                .show()

            dialogBinding.confirmButton.setOnClickListener {
                val product = dialogBinding.inputProduct.text.toString()
                val initialAmount = dialogBinding.inputCurrentAmount.text.toString()
                val finalAmount = dialogBinding.inputFinalAmount.text.toString()

                if (product.isNotBlank() && initialAmount.isNotBlank() && finalAmount.isNotBlank()) {
                    val dInitialAmount = initialAmount.toDouble()
                    val dFinalAmount = finalAmount.toDouble()
                    dialog.dismiss()

                    viewModel.insertGoal(product, dInitialAmount, dFinalAmount)
                } else {
                    Toast.makeText(this, "Adicione todas as informações do produto.", Toast.LENGTH_SHORT).show()
                }
            }

            dialogBinding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun openBundle(){
        val extras = intent.extras
        if(extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if(email != null) {
                viewModel.setEmail(email)
            }
        }
    }

    private fun configRecyclerView(){
        binding.listGoals.layoutManager = LinearLayoutManager(this)
        binding.listGoals.adapter = adapter
    }

    override fun clickDeleteItem(position: Int) {
        TODO("Not yet implemented")
    }

    override fun clickUpdateItem(position: Int) {
        TODO("Not yet implemented")
    }
}