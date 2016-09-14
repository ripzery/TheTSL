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
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.util.Log
import com.google.android.gms.gcm.GcmListenerService
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.ui.main.MainActivity
import com.socket9.thetsl.utils.SharePref
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.json.JSONObject

class MyGcmListenerService : GcmListenerService(), AnkoLogger {

    companion object {
        private val TAG = "MyGcmListenerService"

        val EMERGENCY_CALL = "EMERGENCY CALL"
        val SERVICE_BOOKING = "SERVICE BOOKING"
        val SERVICE_TRACKING = "SERVICE TRACKING"
        val NEW_CAR = "NEW CAR TRACKING"

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

        if (!SharePref.getToken().isNullOrEmpty()) notify(data)
    }

    private fun notify(data: Bundle?) {

        info { data.toString() }

        try {
            val message = data?.getString("message")
            val type = data?.getString("type")

            var messageData: String = message!!

            try {
                val language = getSp(SharePref.SHARE_PREF_KEY_APP_LANG, "th") as String
                messageData = JSONObject(message).getString(language)

            } catch (e: Exception) {
                info { "Can't parse json" }
            }

            var intent: Intent = Intent(this, MainActivity::class.java)

            when (type) {
                EMERGENCY_CALL -> {

                    val statusId: Int = JSONObject(message).getInt("statusId")

                    intent.putExtra("currentFragmentIndex",
                            if (message.equals("เปิดใบแจ้งซ่อมแล้ว") || message.equals("Start service job")) {
                                MainActivity.FRAGMENT_DISPLAY_SERVICE
                            } else {
                                MainActivity.FRAGMENT_DISPLAY_EMERGENCY
                            }
                    )
                            .putExtra("gcmData", Model.GCMData(type, statusId))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                SERVICE_BOOKING -> {

                    val dateBooking = if (SharePref.isEnglish()) {
                        JSONObject(message).getJSONObject("en_data")
                    } else {
                        JSONObject(message).getJSONObject("th_data")
                    }

                    intent.putExtra("currentFragmentIndex", MainActivity.FRAGMENT_DISPLAY_SERVICE)
                            .putExtra("gcmData", Model.GCMData(type, -1, dateBooking.toString()))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                SERVICE_TRACKING -> {
                    intent.putExtra("currentFragmentIndex", MainActivity.FRAGMENT_DISPLAY_SERVICE)
                            .putExtra("gcmData", Model.GCMData(type))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                NEW_CAR -> {
                    intent.putExtra("currentFragmentIndex", MainActivity.FRAGMENT_DISPLAY_CAR_TRACKING)
                            .putExtra("gcmData", Model.GCMData(type))
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
            }

            val intentPending: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)

            val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_noti)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setAutoCancel(true)
                    .setContentIntent(intentPending)
                    .setContentTitle(type)
                    .setContentText(messageData)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .setBigContentTitle(type)
                            .bigText(messageData))
            (getSystemService(GcmListenerService.NOTIFICATION_SERVICE) as NotificationManager).notify(1, mBuilder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
