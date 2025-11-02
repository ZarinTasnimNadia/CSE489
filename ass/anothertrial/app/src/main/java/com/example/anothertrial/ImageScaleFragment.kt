package com.example.anothertrial

import android.widget.Toast
import android.os.Bundle
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import coil.load

class ImageScaleFragment : Fragment(R.layout.fragment_image_scale) {

    private lateinit var imageView: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    private var scaleFactor = 1.0f


    private val imageUrl = "https://i.pinimg.com/1200x/c9/21/19/c92119e130fef7df4e96ea112176570e.jpg"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.scalableImageView)


        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener())

        imageView.setOnTouchListener { v, event ->
            scaleGestureDetector.onTouchEvent(event)
            return@setOnTouchListener true
        }

        // 3. Load the image
        imageView.load(imageUrl) {
            crossfade(true)
            listener(
                onSuccess = { request, result ->
                    Toast.makeText(context, "Image Loaded Successfully", Toast.LENGTH_SHORT).show()
                },
                onError = { request, result ->
                    Toast.makeText(context, "Image Load Failed (URL Access Denied)", Toast.LENGTH_LONG).show()
                }
            )
        }
    }

    /**
     * Inner class to handle the pinch-to-zoom event, stabilizing the image during scaling.
     */
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector): Boolean {
            // 1. Calculate and clamp the scale factor
            scaleFactor *= detector.scaleFactor
            scaleFactor = Math.max(0.5f, Math.min(scaleFactor, 5.0f))


            imageView.pivotX = detector.focusX
            imageView.pivotY = detector.focusY

            // 3. Apply the new scale factor
            imageView.scaleX = scaleFactor
            imageView.scaleY = scaleFactor

            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector) {
            super.onScaleEnd(detector)


            imageView.pivotX = imageView.width / 2f
            imageView.pivotY = imageView.height / 2f
        }
    }
}