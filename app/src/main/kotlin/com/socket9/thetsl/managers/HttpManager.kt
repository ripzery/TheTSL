package com.socket9.thetsl.managers

import com.socket9.thetsl.extensions.getSp
import com.socket9.thetsl.model.Model
import com.socket9.thetsl.network.ApiService
import com.socket9.thetsl.utils.SharePref
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */
object HttpManager{

    // TODO: use compose to subscribeOn, observeOn, and unsubscribeOn (DON'T REPEAT YOURSELF!)

    fun registerUser(email:String, name:String, hometown:String, facebookId:String, fbPhoto:String) : Observable<Model.User>{
        return ApiService.getAPI().registerUser(email, "123456", name, name, email, hometown, "", facebookId, fbPhoto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .unsubscribeOn(Schedulers.io())
    }

    fun loginWithFb(facebookId:String, fbPhoto:String, deviceId: String): Observable<Model.User>{
        return ApiService.getAPI().loginWithFb(facebookId, fbPhoto, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun emergencyCall(lat:String, lng:String, type:String): Observable<Model.BaseModel>{
        return ApiService.getAPI().emergencyCall(SharePref.getToken(), lat, lng, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getProfile(): Observable<Model.Profile> {
        return ApiService.getAPI().getProfile(SharePref.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }
}