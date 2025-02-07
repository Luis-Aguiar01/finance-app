package br.edu.ifsp.dmo.financeapp.ui.activities.goals

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityGoalsBinding
import br.edu.ifsp.dmo.financeapp.databinding.LayoutDialogGoalBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GoalsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGoalsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoalsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configListeners()
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

                if (product.isNotEmpty() && initialAmount.isNotEmpty() && finalAmount.isNotEmpty()) {
                    val dInitialAmount = initialAmount.toDouble()
                    val dFinalAmount = finalAmount.toDouble()
                    dialog.dismiss()
                    Toast.makeText(this, "Product: $product, Initial Amount: R$$dInitialAmount, Final Amount: R$$dFinalAmount", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Adicione todas as informações do produto.", Toast.LENGTH_SHORT).show()
                }
            }

            dialogBinding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }
}