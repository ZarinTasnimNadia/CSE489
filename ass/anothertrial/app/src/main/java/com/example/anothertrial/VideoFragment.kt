package com.example.anothertrial

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.MediaController
import android.widget.VideoView
import android.widget.Toast
import androidx.fragment.app.Fragment

class VideoFragment : Fragment(R.layout.fragment_video) {

    private lateinit var videoView: VideoView
    private lateinit var playButton: Button

    // 1. Define the resource ID for the local file.
    // Assumes the file is named 'video_file.mp4' in res/raw/
    private val VIDEO_RESOURCE_ID = R.raw.video_file

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = view.findViewById(R.id.videoView)
        playButton = view.findViewById(R.id.playVideoButton)

        // 2. FIX: Create the local resource URI (android.resource://package/resource_id)
        val resourceUri = Uri.parse("android.resource://${requireContext().packageName}/$VIDEO_RESOURCE_ID")

        videoView.setVideoURI(resourceUri)
        videoView.setVideoURI(resourceUri)
        // Use setVideoURI with the local resource path
        videoView.setVideoURI(resourceUri)

        // 3. Prepare the Media Controller (standard controls)
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)

        // Optional: Listen for when the video is prepared (finished loading)
        videoView.setOnPreparedListener {
            Toast.makeText(context, "Video is ready to play.", Toast.LENGTH_SHORT).show()
        }

        // 4. Set up the play/pause button click listener
        playButton.setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
                playButton.text = "Resume Video"
            } else {
                // If the video reached the end, rewind it before starting
                if (videoView.currentPosition == videoView.duration) {
                    videoView.seekTo(0)
                }
                videoView.start()
                playButton.text = "Pause Video"
            }
        }

        // 5. Optional: Handle errors
        videoView.setOnErrorListener { mp, what, extra ->
            Toast.makeText(context, "Video Playback Error: $what, Extra: $extra", Toast.LENGTH_LONG).show()
            true
        }
    }

    // 6. Best Practice: Stop playback when the Fragment view is destroyed
    override fun onPause() {
        super.onPause()
        // Pause playback when the user moves away
        videoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop playback and release internal MediaPlayer resources
        videoView.stopPlayback()
        // Clearing the URI helps ensure the internal player is fully released
        videoView.setVideoURI(null)
    }
}