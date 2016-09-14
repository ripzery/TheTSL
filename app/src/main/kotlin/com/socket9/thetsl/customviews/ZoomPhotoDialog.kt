package com.socket9.thetsl.customviews

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.socket9.thetsl.R
import uk.co.senab.photoview.PhotoViewAttacher

/**
 * Created by Euro (ripzery@gmail.com) on 7/14/2016 AD.
 */

class ZoomPhotoDialog : DialogFragment() {

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog.window!!.attributes.windowAnimations = R.style.ZoomImageDialogAnimation
    }

    private fun initInstance() {
        isCancelable = true
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        photoViewAttacher = PhotoViewAttacher(ivZoomPhoto)
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
