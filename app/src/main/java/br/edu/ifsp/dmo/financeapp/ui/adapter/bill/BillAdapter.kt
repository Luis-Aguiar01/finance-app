package br.edu.ifsp.dmo.financeapp.ui.adapter.bill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.databinding.ItemFixedBillListBinding
import br.edu.ifsp.dmo.financeapp.ui.listeners.bill.BillItemClickListener

class BillAdapter(
    private val listener: BillItemClickListener
) : RecyclerView.Adapter<BillAdapter.ViewHolder>() {

    private var database: List<Bill> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fixed_bill_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bill = database[position]

        holder.binding.descriptionItem.text = bill.name
        holder.binding.productValue.text = bill.value.toString()

        holder.binding.editButton.setOnClickListener {
            listener.clickUpdateItem(bill.id)
        }

        holder.binding.removeButton.setOnClickListener {
            listener.clickDeleteItem(bill.id)
        }
    }

    override fun getItemCount(): Int {
        return database.size
    }

    fun getDatasetItem(position: Int): Bill {
        return database[position]
    }

    fun submitDatabase(data: List<Bill>) {
        database = data
        this.notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemFixedBillListBinding = ItemFixedBillListBinding.bind(view)
    }
}