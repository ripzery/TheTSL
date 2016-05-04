package com.socket9.thetsl.utils

import android.content.Context
import com.afollestad.materialdialogs.MaterialDialog
import com.socket9.thetsl.R
import com.socket9.thetsl.extensions.getSp

/**
 * Created by Euro (ripzery@gmail.com) on 10/7/15 AD.
 */
object DialogUtil {
    fun getForgotDialog(context: Context, callback: MaterialDialog.InputCallback): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_forgot_pw_title)).content(getString(context, R.string.dialog_forgot_pw_content)).positiveText(getString(context, R.string.dialog_forgot_pw_positive)).negativeText(getString(context, R.string.dialog_forgot_pw_negative)).input(getString(context, R.string.dialog_forgot_pw_input_hint), "", false, callback).build()
    }

    fun getCallUsDialog(context: Context, callback: MaterialDialog.SingleButtonCallback): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_call_us_title)).content(getString(context, R.string.dialog_call_us_content)).positiveText(getString(context, R.string.dialog_call_us_positive)).negativeText(getString(context, R.string.dialog_call_us_negative)).onPositive(callback).build()
    }

    fun getUpdateProfileDialog(context: Context, callback: MaterialDialog.SingleButtonCallback): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_update_profile_title)).content(getString(context, R.string.dialog_update_profile_content)).positiveText(getString(context, R.string.dialog_update_profile_positive)).negativeText(getString(context, R.string.dialog_update_profile_negative)).onPositive(callback).build()
    }

    fun getSignOutDialog(context: Context, callback: MaterialDialog.SingleButtonCallback): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_signout_title)).content(getString(context, R.string.dialog_signout_content)).positiveText(getString(context, R.string.dialog_signout_positive)).negativeText(getString(context, R.string.dialog_signout_negative)).onPositive(callback).build()
    }

    fun getQuitDialog(context: Context, callback: MaterialDialog.SingleButtonCallback): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_quit_title)).content(getString(context, R.string.dialog_quit_content)).positiveText(getString(context, R.string.dialog_quit_positive)).negativeText(getString(context, R.string.dialog_quit_negative)).onPositive(callback).build()
    }

    fun getChangeLangDialog(context: Context, callback: MaterialDialog.ListCallbackSingleChoice): MaterialDialog {
        return MaterialDialog.Builder(context).title(getString(context, R.string.dialog_change_lang_title)).items(R.array.change_language).itemsCallbackSingleChoice(if (getSp(SharePref.SHARE_PREF_KEY_APP_LANG, "").equals("en")) 1 else 0, callback).negativeText(getString(context, R.string.dialog_change_lang_cancel)).positiveText(getString(context, R.string.dialog_change_lang_choose)).build()
    }

    private fun getString(context: Context, id: Int): String {
        return context.getString(id)
    }
}
