package com.example.myvehicles

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.myvehicles.databinding.FragmentSecondBinding

class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VehicleViewModel by activityViewModels()
    private var editingPosition: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up the AutoCompleteTextView for models
        val modelAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_dropdown_item_1line, mutableListOf())
        binding.editModel.setAdapter(modelAdapter)

        viewModel.models.observe(viewLifecycleOwner) { models ->
            modelAdapter.clear()
            modelAdapter.addAll(models)
            modelAdapter.notifyDataSetChanged()
        }

        // Fetch models when the make changes
        binding.editMake.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val make = s.toString().trim()
                if (make.length >= 2) {
                    viewModel.fetchModelsForMake(make)
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        viewModel.editingVehicle.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                val (position, vehicle) = pair
                editingPosition = position
                binding.editNickname.setText(vehicle.nickname)
                binding.editMake.setText(vehicle.make)
                binding.editModel.setText(vehicle.model)
                binding.editYear.setText(vehicle.year)
                binding.btnAddVehicle.text = "Update Vehicle"
            } else {
                editingPosition = null
                clearInputs()
                binding.btnAddVehicle.text = "Add Vehicle"
            }
        }

        binding.btnAddVehicle.setOnClickListener {
            val nickname = binding.editNickname.text.toString()
            val make = binding.editMake.text.toString()
            val model = binding.editModel.text.toString()
            val year = binding.editYear.text.toString()

            if (nickname.isNotEmpty() && make.isNotEmpty() && model.isNotEmpty() && year.isNotEmpty()) {
                val vehicle = Vehicle(nickname, make, model, year)
                
                val pos = editingPosition
                if (pos != null) {
                    viewModel.updateVehicle(pos, vehicle)
                    Toast.makeText(context, "Vehicle updated!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.addVehicle(vehicle)
                    Toast.makeText(context, "Vehicle added!", Toast.LENGTH_SHORT).show()
                }
                
                clearInputs()
                viewModel.setEditingVehicle(-1, null)
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun clearInputs() {
        binding.editNickname.text?.clear()
        binding.editMake.text?.clear()
        binding.editModel.text?.clear()
        binding.editYear.text?.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
