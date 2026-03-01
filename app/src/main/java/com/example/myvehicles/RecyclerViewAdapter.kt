package com.example.myvehicles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MyAdapter(private var fullList: MutableList<Vehicle>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var displayList: MutableList<Vehicle> = fullList.toMutableList()
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
        val vehicle = displayList[position]
        holder.nickname.text = vehicle.nickname
        holder.year.text = "Year: ${vehicle.year}"
        holder.maker.text = "Make: ${vehicle.make}"
        holder.model.text = "Model: ${vehicle.model}"

        holder.detailsContainer.visibility = if (expandedPositions.contains(position)) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun getItemCount(): Int = displayList.size

    fun removeItem(position: Int) {
        if (position in displayList.indices) {
            val vehicle = displayList[position]
            displayList.removeAt(position)
            fullList.remove(vehicle)
            expandedPositions.remove(position)
            notifyItemRemoved(position)
        }
    }

    fun updateData(newItems: List<Vehicle>) {
        fullList = newItems.toMutableList()
        displayList = fullList.toMutableList()
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        displayList = if (query.isEmpty()) {
            fullList.toMutableList()
        } else {
            val resultList = mutableListOf<Vehicle>()
            for (vehicle in fullList) {
                if (vehicle.nickname.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) ||
                    vehicle.make.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT)) ||
                    vehicle.model.lowercase(Locale.ROOT).contains(query.lowercase(Locale.ROOT))
                ) {
                    resultList.add(vehicle)
                }
            }
            resultList
        }
        notifyDataSetChanged()
    }
}
