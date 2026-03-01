package com.example.myvehicles

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class VehicleViewModel : ViewModel() {
    private val _vehicles = MutableLiveData<MutableList<Vehicle>>(
        mutableListOf(
            Vehicle("Bessie", "Ford", "Focus", "2014"),
            Vehicle("Junk", "Chevrolet", "Blazer", "1999"),
            Vehicle("Old Trusty", "Chevrolet", "Trail Blazer", "2023")
        )
    )
    val vehicles: LiveData<MutableList<Vehicle>> = _vehicles

    private val _editingVehicle = MutableLiveData<Pair<Int, Vehicle>?>()
    val editingVehicle: LiveData<Pair<Int, Vehicle>?> = _editingVehicle

    fun addVehicle(vehicle: Vehicle) {
        val currentList = _vehicles.value ?: mutableListOf()
        currentList.add(vehicle)
        _vehicles.value = currentList
    }

    fun removeVehicle(position: Int) {
        val currentList = _vehicles.value ?: return
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _vehicles.value = currentList
        }
    }

    fun setEditingVehicle(position: Int, vehicle: Vehicle?) {
        if (vehicle == null) {
            _editingVehicle.value = null
        } else {
            _editingVehicle.value = Pair(position, vehicle)
        }
    }

    fun updateVehicle(position: Int, vehicle: Vehicle) {
        val currentList = _vehicles.value ?: return
        if (position in currentList.indices) {
            currentList[position] = vehicle
            _vehicles.value = currentList
            _editingVehicle.value = null
        }
    }
}
