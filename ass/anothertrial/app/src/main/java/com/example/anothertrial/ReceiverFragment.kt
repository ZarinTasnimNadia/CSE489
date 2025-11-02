package com.example.anothertrial

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class ReceiverFragment : Fragment(R.layout.fragment_receiver) {

    private lateinit var statusTextView: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        statusTextView = view.findViewById(R.id.statusText)

        // 1. Check if arguments (message data) were passed
        val message = arguments?.getString("message_data")

        if (message != null) {

            statusTextView.text = getString(R.string.message_received_format, message)
        } else {

            statusTextView.text = getString(R.string.status_waiting_broadcast)
        }
    }
}