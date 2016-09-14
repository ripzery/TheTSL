package com.socket9.thetsl.customviews

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import com.socket9.thetsl.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by Euro (ripzery@gmail.com) on 7/14/2016 AD.
 */

class ZoomPhotoDialog : DialogFragment(), AnkoLogger {

    private var path = ""
    private var ivZoomPhoto: ImageView? = null
    private var ivClose: ImageView? = null
    private var photoViewAttacher: PhotoViewAttacher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = LayoutInflater.from(context).inflate(R.layout.layout_zoom_photo, container, false)

        ivZoomPhoto = v.findViewById(R.id.ivZoomImage) as ImageView
        ivClose = v.findViewById(R.id.ivClose) as ImageView

        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInstance()
    }

    override fun onResume() {
        // Get existing layout params for the window
        val params = dialog.window.attributes
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT
        params.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window.attributes = params as WindowManager.LayoutParams
        // Call super onResume after sizing
        super.onResume()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window!!.attributes.windowAnimations = R.style.ZoomImageDialogAnimation
    }

    private fun initInstance() {
        isCancelable = true
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        photoViewAttacher = PhotoViewAttacher(ivZoomPhoto)
        photoViewAttacher!!.setZoomInterpolator(AccelerateDecelerateInterpolator())
        photoViewAttacher!!.setOnMatrixChangeListener {
            info{ it.top }
            ivClose!!.y = it.top
        }
        ivClose!!.setOnClickListener { this@ZoomPhotoDialog.dismiss() }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

    companion object {

        fun newInstance(): ZoomPhotoDialog {
            val args = Bundle()
            val fragment = ZoomPhotoDialog()
            fragment.arguments = args
            return fragment
        }
    }
}
