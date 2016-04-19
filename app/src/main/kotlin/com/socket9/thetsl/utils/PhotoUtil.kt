package com.socket9.thetsl.utils

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import android.util.TypedValue
import java.io.ByteArrayOutputStream

/**
 * Created by Euro on 10/8/15 AD.
 */
object PhotoUtil {
    private fun compress(bitmap: Bitmap, percent: Int): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, percent, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    private fun encode(byteArray: ByteArray): String {
        return "data:image/png;base64," + Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    fun compressThenEncoded(bitmap: Bitmap, percent: Int): String {
        return encode(compress(bitmap, percent))
    }

    fun convertDpToPx(context: Context, dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
    }
}
