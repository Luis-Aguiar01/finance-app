package br.edu.ifsp.dmo.financeapp.ui.activities.purchase

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.databinding.ActivityAddPurchaseBinding
import br.edu.ifsp.dmo.financeapp.databinding.LayoutDialogAddPurchaseBinding
import br.edu.ifsp.dmo.financeapp.databinding.LayoutDialogEditPurchaseBinding
import br.edu.ifsp.dmo.financeapp.ui.adapter.bill.BillAdapter
import br.edu.ifsp.dmo.financeapp.ui.listeners.bill.BillItemClickListener
import br.edu.ifsp.dmo.financeapp.util.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddPurchaseActivity : AppCompatActivity(), BillItemClickListener {

    private lateinit var binding: ActivityAddPurchaseBinding
    private lateinit var viewModel: AddPurchaseViewModel
    private val adapter = BillAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPurchaseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(AddPurchaseViewModel::class.java)

        openBundle()
        configListeners()
        configRecyclerView()
        configObservers()
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
                    viewModel.insertPurchase(product, price.toDouble(), category)
                    dialog.dismiss()
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.add_all_product_informations_error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            dialogBinding.cancelButton.setOnClickListener {
                dialog.dismiss()
            }
        }
    }

    private fun configRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun configObservers() {
        viewModel.inserted.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.bill_added_successfully),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.deleted.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.bill_deleted_successfuly), Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.updated.observe(this, Observer {
            if (it) {
                Toast.makeText(
                    this,
                    getString(R.string.bill_updated_successfully), Toast.LENGTH_SHORT
                ).show()
            }
        })

        viewModel.bills.observe(this, Observer {
            adapter.submitDatabase(it)
        })
    }

    private fun openBundle() {
        val extras = intent.extras
        if (extras != null) {
            val email = extras.getString(Constants.USER_EMAIL)
            if (email != null) {
                viewModel.setEmail(email)
            }
        }
    }

    override fun clickDeleteItem(id: Long) {
        viewModel.deletePurchase(id)
    }

    override fun clickUpdateItem(id: Long) {
        viewModel.clearSelectedBill()
        viewModel.getBillById(id)

        viewModel.selectedBill.observe(this) { bill ->
            if (bill != null) {
                val dialogBinding = LayoutDialogEditPurchaseBinding.inflate(layoutInflater)
                val dialog = MaterialAlertDialogBuilder(this, R.style.CustomDialogTheme)
                    .setView(dialogBinding.root)
                    .show()

                dialogBinding.inputProduct.setText(bill.name)
                dialogBinding.inputPrice.setText(bill.value.toString())
                dialogBinding.inputCategory.setText(bill.category, false)

                dialogBinding.confirmButton.setOnClickListener {
                    val newProduct = dialogBinding.inputProduct.text.toString()
                    val newPrice = dialogBinding.inputPrice.text.toString()
                    val newCategory = dialogBinding.inputCategory.text.toString()

                    if (newProduct.isNotEmpty() && newPrice.isNotEmpty() && newCategory.isNotEmpty()) {
                        dialog.dismiss()
                        viewModel.updatePurchase(id, newProduct, newCategory, newPrice.toDouble())
                    } else {
                        Toast.makeText(
                            this,
                            getString(R.string.fil_out_all_fields_error), Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                dialogBinding.cancelButton.setOnClickListener {
                    dialog.dismiss()
                }
                viewModel.selectedBill.removeObservers(this)
            }
        }
    }
}