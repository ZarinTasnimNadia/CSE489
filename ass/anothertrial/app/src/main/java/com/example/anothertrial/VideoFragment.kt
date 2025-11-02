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


    private val VIDEO_RESOURCE_ID = R.raw.video_file

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoView = view.findViewById(R.id.videoView)
        playButton = view.findViewById(R.id.playVideoButton)


        val resourceUri = Uri.parse("android.resource://${requireContext().packageName}/$VIDEO_RESOURCE_ID")

        videoView.setVideoURI(resourceUri)
        videoView.setVideoURI(resourceUri)

        videoView.setVideoURI(resourceUri)

        // 3. Prepare the Media Controller (standard controls)
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(videoView)
        videoView.setMediaController(mediaController)


        videoView.setOnPreparedListener {
            Toast.makeText(context, "Video is ready to play.", Toast.LENGTH_SHORT).show()
        }


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


        videoView.setOnErrorListener { mp, what, extra ->
            Toast.makeText(context, "Video Playback Error: $what, Extra: $extra", Toast.LENGTH_LONG).show()
            true
        }
    }


    override fun onPause() {
        super.onPause()

        videoView.pause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        videoView.stopPlayback()

        videoView.setVideoURI(null)
    }
}