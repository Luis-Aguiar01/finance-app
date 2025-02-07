package br.edu.ifsp.dmo.financeapp.ui.activities.bills

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityFixedBillsBinding
import br.edu.ifsp.dmo.financeapp.databinding.LayoutDialogAddPurchaseBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class FixedBillsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFixedBillsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFixedBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        configListeners()
    }

    private fun configListeners() {
        binding.arrowBack.setOnClickListener {
            finish()
        }

        binding.addButton.setOnClickListener {
            val dialogBinding = LayoutDialogAddPurchaseBinding.inflate(layoutInflater)

            val dialog = MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                .setView(dialogBinding.root)
                .show()

            dialogBinding.confirmButton.setOnClickListener {
                val product = dialogBinding.inputProduct.text.toString()
                val price = dialogBinding.inputPrice.text.toString()
                val category = dialogBinding.inputCategory.text.toString()

                if (product.isNotEmpty() && price.isNotEmpty() && category.isNotEmpty()) {
                    val dPrice = price.toDouble()
                    dialog.dismiss()
                    Toast.makeText(this, "Produto: $product, Price: R$$dPrice, Category: $category", Toast.LENGTH_SHORT).show()
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