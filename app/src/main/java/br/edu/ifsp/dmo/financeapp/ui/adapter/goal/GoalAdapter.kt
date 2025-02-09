package br.edu.ifsp.dmo.financeapp.ui.adapter.goal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.edu.ifsp.dmo.financeapp.R
import br.edu.ifsp.dmo.financeapp.data.entity.goal.Goal
import br.edu.ifsp.dmo.financeapp.databinding.ItemGoalListBinding
import br.edu.ifsp.dmo.financeapp.ui.listeners.goal.GoalItemClickListener

class GoalAdapter(private val listener: GoalItemClickListener):
    RecyclerView.Adapter<GoalAdapter.ViewHolder>() {

    private var database: List<Goal> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_goal_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: GoalAdapter.ViewHolder, position: Int) {
        val goal = database[position]

        holder.binding.descriptionItem.text = goal.name
        holder.binding.initialValue.text = goal.accumulated_value.toString()
        holder.binding.finalValue.text = goal.target_value.toString()
        holder.binding.progressIndicator.max = goal.target_value.toInt()
        holder.binding.progressIndicator.setProgress(goal.accumulated_value.toInt(), true)

        holder.binding.addButton.setOnClickListener {
            listener.clickUpdateItem(goal.id)
        }

        holder.binding.removeButton.setOnClickListener {
            listener.clickDeleteItem(goal.id)
        }
    }

    override fun getItemCount(): Int {
        return database.size
    }

    fun getDatasetItem(position: Int): Goal {
        return database[position]
    }

    fun submitDatabase(data: List<Goal>) {
        database = data
        this.notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding: ItemGoalListBinding = ItemGoalListBinding.bind(view)
    }

}