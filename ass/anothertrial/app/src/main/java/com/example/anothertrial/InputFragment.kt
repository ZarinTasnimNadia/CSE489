package com.example.anothertrial

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment

class InputFragment : Fragment(R.layout.fragment_input) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val messageInput: EditText = view.findViewById(R.id.messageInput)
        val sendButton: Button = view.findViewById(R.id.sendBroadcastButton)

        sendButton.setOnClickListener {
            val message = messageInput.text.toString()
            if (message.isNotEmpty()) {

                // 1. Create ReceiverFragment and Bundle to pass data directly
                val receiverFragment = ReceiverFragment()
                val args = Bundle().apply {
                    putString("message_data", message)
                }

                // 2. Attach message as arguments
                receiverFragment.arguments = args

                // 3. Navigate to the Receiver screen
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, receiverFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}