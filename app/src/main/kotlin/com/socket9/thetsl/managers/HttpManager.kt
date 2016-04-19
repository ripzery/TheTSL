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

    fun login(email:String, password:String, deviceId: String) : Observable<Model.User>{
        return ApiService.getAPI().login(email, password, deviceId)
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

    fun updateProfile(nameEn: String, nameTh:String, phone:String, address:String, picture:String) : Observable<Model.BaseModel>{
        return ApiService.getAPI().updateProfile(SharePref.getToken(), nameEn, nameTh, phone, address, picture)
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

    fun getListNews() : Observable<Model.ListNewsEvent>{
        return ApiService.getAPI().getListNews(SharePref.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun forgetPassword(email:String) : Observable<Model.BaseModel>{
        return ApiService.getAPI().forgetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getNews(newsId:Int) : Observable<Model.NewsEvent>{
        return ApiService.getAPI().getNews(SharePref.getToken(), newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getListEvent() : Observable<Model.ListNewsEvent>{
        return ApiService.getAPI().getListEvents(SharePref.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getEvent(eventId : Int): Observable<Model.NewsEvent>{
        return ApiService.getAPI().getEvent(SharePref.getToken(), eventId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getListContact() : Observable<Model.ListContacts> {
        return ApiService.getAPI().getListContacts(SharePref.getToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun uploadPhoto(path:String) : Observable<Model.Photo>{
        return ApiService.getAPI().uploadPhoto(SharePref.getToken(), path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun updatePicture(picture: String) : Observable<Model.BaseModel>{
        return ApiService.getAPI().updatePicture(SharePref.getToken(), picture)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }
}