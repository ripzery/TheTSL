/**
 * Copyright 2015 Google Inc. All Rights Reserved.

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.socket9.thetsl.gcm

import android.app.Notification
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.android.gms.gcm.GcmListenerService
import com.socket9.thetsl.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.notificationManager

class MyGcmListenerService : GcmListenerService(), AnkoLogger {

    companion object {
        private val TAG = "MyGcmListenerService"

        val EMERGENCY_CALL = "EMERGENCY CALL"
        val SERVICE_BOOKING = "SERVICE BOOKING"
        val SERVICE_TRACKING = "SERVICE TRACKING"
        val NEW_CAR = "NEW CAR"

    }


    /**
     * Called when message is received.

     * @param from SenderID of the sender.
     * *
     * @param data Data bundle containing message data as key/value pairs.
     * * For Set of keys use data.keySet().
     */
    // [START receive_message]
    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data!!.getString("message")
        Log.d(TAG, "From: " + from!!)
        Log.d(TAG, "Message: " + message)

        notify(data)
    }

    private fun notify(data: Bundle?) {

        info { data.toString() }

        try {
            val message = data?.getString("message")
            val type = data?.getString("type")

            when (type) {
                EMERGENCY_CALL -> {
                }
                SERVICE_BOOKING -> {
                }
                SERVICE_TRACKING -> {
                }
                NEW_CAR -> {
                }
            }

            val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setContentTitle("Hello")
                    .setContentText(message);

            notificationManager.notify(1, mBuilder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}
