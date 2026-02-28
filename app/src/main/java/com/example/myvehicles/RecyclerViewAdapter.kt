package com.example.myvehicles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for a list of Vehicles
class MyAdapter(private var items: MutableList<Vehicle>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.itemTextNickname)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val vehicle = items[position]
        holder.textView.text = "${vehicle.nickname}: ${vehicle.year} ${vehicle.make} ${vehicle.model}"
    }

    override fun getItemCount(): Int = items.size

    fun removeItem(position: Int) {
        if (position in items.indices) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newItems: List<Vehicle>) {
        items = newItems.toMutableList()
        notifyDataSetChanged()
    }
}
