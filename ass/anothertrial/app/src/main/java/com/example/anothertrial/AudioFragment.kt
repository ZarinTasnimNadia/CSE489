package com.example.anothertrial

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.util.concurrent.TimeUnit

class AudioFragment : Fragment(R.layout.fragment_audio) {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var audioSeekBar: SeekBar
    private lateinit var currentDurationText: TextView
    private lateinit var totalDurationText: TextView

    // Handler and Runnable for updating the SeekBar/time every 500ms
    private val handler = Handler(Looper.getMainLooper())
    private val updateSeekBar = object : Runnable {
        override fun run() {
            mediaPlayer?.let { player ->
                val currentPosition = player.currentPosition
                audioSeekBar.progress = currentPosition
                currentDurationText.text = formatTime(currentPosition)
            }
            // Schedule this runnable to run again after 500ms
            handler.postDelayed(this, 500)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playButton: Button = view.findViewById(R.id.playAudioButton)
        val pauseButton: Button = view.findViewById(R.id.pauseAudioButton)
        val stopButton: Button = view.findViewById(R.id.stopAudioButton)
        audioSeekBar = view.findViewById(R.id.audioSeekBar)
        currentDurationText = view.findViewById(R.id.currentDurationText)
        totalDurationText = view.findViewById(R.id.totalDurationText)

        initializeMediaPlayer(view.context)

        // 1. Play Button Logic
        playButton.setOnClickListener {
            mediaPlayer?.let { player ->
                if (!player.isPlaying) {
                    player.start()
                    // Start updating the seek bar
                    handler.post(updateSeekBar)
                    Toast.makeText(context, "Playing Audio", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 2. Pause Button Logic
        pauseButton.setOnClickListener {
            mediaPlayer?.let { player ->
                if (player.isPlaying) {
                    player.pause()
                    // Stop updating the seek bar while paused
                    handler.removeCallbacks(updateSeekBar)
                    Toast.makeText(context, "Paused Audio", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // 3. Stop Button Logic
        stopButton.setOnClickListener {
            mediaPlayer?.let { player ->
                player.stop()
                player.release() // Release resources
                mediaPlayer = null

                // Reset UI
                audioSeekBar.progress = 0
                currentDurationText.text = formatTime(0)
                handler.removeCallbacks(updateSeekBar)

                Toast.makeText(context, "Stopped Audio", Toast.LENGTH_SHORT).show()
                initializeMediaPlayer(view.context) // Re-initialize for next play
            }
        }

        // 4. Seek Bar Change Listener (Allows user to drag the slider)
        audioSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    // Only seek if the change was initiated by the user dragging the bar
                    mediaPlayer?.seekTo(progress)
                    currentDurationText.text = formatTime(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    // Initialize/Re-initialize the MediaPlayer
    private fun initializeMediaPlayer(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.audio_file)

            mediaPlayer?.setOnPreparedListener { player ->
                val duration = player.duration
                audioSeekBar.max = duration
                totalDurationText.text = formatTime(duration)

                // Set completion listener to reset UI when audio finishes
                player.setOnCompletionListener {
                    audioSeekBar.progress = 0
                    currentDurationText.text = formatTime(0)
                    handler.removeCallbacks(updateSeekBar)
                    player.seekTo(0)
                    player.pause() // Keep controls usable
                }
            }
        }
    }

    // Utility function to format milliseconds to "m:ss" format
    private fun formatTime(millis: Int): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis.toLong())
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis.toLong()) -
                TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%d:%02d", minutes, seconds)
    }

    // IMPORTANT: Release resources when the Fragment view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        // Stop the seekbar update handler
        handler.removeCallbacks(updateSeekBar)

        // Release the MediaPlayer
        mediaPlayer?.release()
        mediaPlayer = null
    }
}