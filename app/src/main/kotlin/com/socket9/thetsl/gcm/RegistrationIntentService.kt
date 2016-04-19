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

import android.app.IntentService
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.utils.SharePref

class RegistrationIntentService : IntentService {


    constructor(name: String) : super(name){

    }

    constructor() : super("RegistrationIntentService"){

    }

    override fun onHandleIntent(intent: Intent) {
        try {
            val instanceID = InstanceID.getInstance(this)
            val token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)
            Log.i(TAG, "GCM Registration Token: " + token)

            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToSharePref(token)

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            // [END register_for_gcm]
        } catch (e: Exception) {
            Log.d(TAG, "Failed to complete token refresh", e)
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            //sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }

        val registrationComplete:Intent = Intent(REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.

     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.

     * @param token The new token.
     */
    private fun sendRegistrationToSharePref(token: String) {
        saveSp(SharePref.SHARE_PREF_KEY_GCM_TOKEN, token)
    }

    companion object {
        private val TAG = "RegIntentService"
        val REGISTRATION_COMPLETE: String = "REGISTER_COMPLETE"
    }

}
