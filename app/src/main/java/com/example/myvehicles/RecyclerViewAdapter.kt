package com.example.myvehicles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private var items: MutableList<Vehicle>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    // Track which items are expanded
    private val expandedPositions = mutableSetOf<Int>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nickname: TextView = itemView.findViewById(R.id.itemTextNickname)
        val year: TextView = itemView.findViewById(R.id.textViewYear)
        val maker: TextView = itemView.findViewById(R.id.textViewMaker)
        val model: TextView = itemView.findViewById(R.id.textViewModel)
        val detailsContainer: LinearLayout = itemView.findViewById(R.id.detailsContainer)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (expandedPositions.contains(position)) {
                        expandedPositions.remove(position)
                    } else {
                        expandedPositions.add(position)
                    }
                    notifyItemChanged(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicle = items[position]
        holder.nickname.text = vehicle.nickname
        holder.year.text = "Year: ${vehicle.year}"
        holder.maker.text = "Make: ${vehicle.make}"
        holder.model.text = "Model: ${vehicle.model}"

        // Show/hide details based on expanded state
        holder.detailsContainer.visibility = if (expandedPositions.contains(position)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            expandedPositions.remove(position) // Clean up expanded state
            notifyItemRemoved(position)
        }
    }

    fun updateData(newItems: List<Vehicle>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
