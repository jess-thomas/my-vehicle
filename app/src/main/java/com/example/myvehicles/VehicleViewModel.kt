package com.example.myvehicles

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VehicleViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = application.getSharedPreferences("vehicle_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://vpic.nhtsa.dot.gov/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(VehicleApiService::class.java)

    private val _vehicles = MutableLiveData<MutableList<Vehicle>>(loadVehicles())
    val vehicles: LiveData<MutableList<Vehicle>> = _vehicles

    private val _editingVehicle = MutableLiveData<Pair<Int, Vehicle>?>()
    val editingVehicle: LiveData<Pair<Int, Vehicle>?> = _editingVehicle

    private val _models = MutableLiveData<List<String>>()
    val models: LiveData<List<String>> = _models

    private fun loadVehicles(): MutableList<Vehicle> {
        val json = sharedPreferences.getString("vehicle_list", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Vehicle>>() {}.type
            gson.fromJson(json, type)
        } else {
            mutableListOf()
        }
    }

    private fun saveVehicles(list: MutableList<Vehicle>) {
        val json = gson.toJson(list)
        sharedPreferences.edit().putString("vehicle_list", json).apply()
    }

    fun addVehicle(vehicle: Vehicle) {
        val currentList = _vehicles.value ?: mutableListOf()
        currentList.add(vehicle)
        _vehicles.value = currentList
        saveVehicles(currentList)
    }

    fun removeVehicle(position: Int) {
        val currentList = _vehicles.value ?: return
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _vehicles.value = currentList
            saveVehicles(currentList)
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
            saveVehicles(currentList)
        }
    }

    fun fetchModelsForMake(make: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getModelsForMake(make)
                val modelNames = response.Results.map { it.Model_Name }
                _models.value = modelNames
            } catch (e: Exception) {
                _models.value = emptyList()
            }
        }
    }
}
