package br.edu.ifsp.dmo.financeapp.ui.adapter.historical


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.data.entity.bill.Bill
import br.edu.ifsp.dmo.financeapp.databinding.ItemRecentPurchaseBinding

class HistoricalAdapter():
    RecyclerView.Adapter<HistoricalAdapter.ViewHolder>() {

    private var database: List<Bill> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HistoricalAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recent_purchase, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: HistoricalAdapter.ViewHolder, position: Int) {
        val bill = database[position]

        holder.binding.descriptionItem.text = bill.name
        holder.binding.productValue.text = bill.value.toString()
        holder.binding.date.text = bill.date.toString()

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
        val binding: ItemRecentPurchaseBinding = ItemRecentPurchaseBinding.bind(view)
    }
}