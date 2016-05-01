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

import android.os.Bundle
import android.util.Log
import com.google.android.gms.gcm.GcmListenerService

class MyGcmListenerService : GcmListenerService() {

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
    }

    companion object {

        private val TAG = "MyGcmListenerService"
    }
}