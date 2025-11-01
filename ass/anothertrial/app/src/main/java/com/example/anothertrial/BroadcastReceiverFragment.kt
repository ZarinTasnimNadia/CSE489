package com.example.anothertrial

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment

class BroadcastReceiverFragment : Fragment(R.layout.fragment_broadcast_receiver) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val broadcastSpinner: Spinner = view.findViewById(R.id.broadcastSpinner)
        val proceedButton: Button = view.findViewById(R.id.proceedButton)

        // 1. Populate the Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.broadcast_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        broadcastSpinner.adapter = adapter

        proceedButton.setOnClickListener {
            val selectedOption = broadcastSpinner.selectedItem.toString()

            val nextFragment = when (selectedOption) {
                "Custom broadcast receiver" -> InputFragment()
                "System battery notification receiver" -> BatteryFragment()
                else -> return@setOnClickListener
            }

            // 2. Navigate to the next fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, nextFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}