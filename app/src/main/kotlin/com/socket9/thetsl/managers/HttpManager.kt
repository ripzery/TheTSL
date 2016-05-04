package com.socket9.thetsl.managers

import android.content.Intent
import com.socket9.thetsl.SignInActivity
import com.socket9.thetsl.extensions.saveSp
import com.socket9.thetsl.models.Model
import com.socket9.thetsl.network.ApiService
import com.socket9.thetsl.utils.Contextor
import com.socket9.thetsl.utils.SharePref
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */
object HttpManager {

    // TODO: use compose to subscribeOn, observeOn, and unsubscribeOn (DON'T REPEAT YOURSELF!)

    fun registerUser(email: String, name: String, hometown: String, facebookId: String, fbPhoto: String): Observable<Model.User> {
        return ApiService.getAPI().registerUser(email, "123456", name, name, email, hometown, "", facebookId, fbPhoto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun login(email: String, password: String, deviceId: String): Observable<Model.User> {
        return ApiService.getAPI().login(email, password, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun loginWithFb(facebookId: String, fbPhoto: String, deviceId: String): Observable<Model.User> {
        return ApiService.getAPI().loginWithFb(facebookId, fbPhoto, deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun saveDeviceId(deviceId: String): Observable<Model.BaseModel> {
        return ApiService.getAPI().saveDeviceByToken(SharePref.getToken(), deviceId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun emergencyCall(lat: String, lng: String, type: String): Observable<Model.BaseModel> {
        return ApiService.getAPI().emergencyCall(SharePref.getToken(), lat, lng, type)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun updateProfile(nameEn: String, nameTh: String, password: String, phone: String, address: String, picture: String): Observable<Model.BaseModel> {
        return ApiService.getAPI().updateProfile(SharePref.getToken(), nameEn, nameTh, password, phone, address, picture)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getProfile(): Observable<Model.Profile> {
        return ApiService.getAPI().getProfile(SharePref.getToken())
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getListNews(): Observable<Model.ListNewsEvent> {
        return ApiService.getAPI().getListNews(SharePref.getToken())
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun forgetPassword(email: String): Observable<Model.BaseModel> {
        return ApiService.getAPI().forgetPassword(email)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getNews(newsId: Int): Observable<Model.NewsEvent> {
        return ApiService.getAPI().getNews(SharePref.getToken(), newsId)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getListEvent(): Observable<Model.ListNewsEvent> {
        return ApiService.getAPI().getListEvents(SharePref.getToken())
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getEvent(eventId: Int): Observable<Model.NewsEvent> {
        return ApiService.getAPI().getEvent(SharePref.getToken(), eventId)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getListContact(): Observable<Model.ListContacts> {
        return ApiService.getAPI().getListContacts(SharePref.getToken())
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getContact(contactId: Int): Observable<Model.Contact> {
        return ApiService.getAPI().getContact(SharePref.getToken(), contactId)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun uploadPhoto(path: String): Observable<Model.Photo> {
        return ApiService.getAPI().uploadPhoto(SharePref.getToken(), path)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun updatePicture(picture: String): Observable<Model.BaseModel> {
        return ApiService.getAPI().updatePicture(SharePref.getToken(), picture)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getServiceBasicData(): Observable<Model.ServiceBasicData> {
        return ApiService.getAPI().getServiceBasicData(SharePref.getToken())
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun bookService(newBooking: Model.NewBooking): Observable<Model.BaseModel> {
        return ApiService.getAPI().bookService(SharePref.getToken(),
                newBooking.licensePlate,
                newBooking.date,
                newBooking.model.id,
                newBooking.type.id,
                newBooking.note,
                newBooking.phone,
                newBooking.branch.id)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getServiceBookingList(order: String): Observable<Model.ServiceBookingList> {
        return ApiService.getAPI().getServiceBookingList(SharePref.getToken(), order)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun getServiceTrackingList(order: String): Observable<Model.ServiceTrackingList> {
        return ApiService.getAPI().getServiceTrackingList(SharePref.getToken(), order)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    fun serviceCarTracking(serviceJobNumber: String, trackingId: String): Observable<Model.ServiceTrackingList> {
        return ApiService.getAPI().serviceCarTracking(SharePref.getToken(), serviceJobNumber, trackingId)
                .doOnNext {
                    checkToken(it.result, it.message)
                }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
    }

    private fun checkToken(result: Boolean, message: String? = null) {
        try {
            if (!result && message!!.contains("token")) {
                saveSp(SharePref.SHARE_PREF_KEY_API_TOKEN, "")
                Contextor.context!!.startActivity(Intent(Contextor.context, SignInActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("invalidToken", true))

            }
        }catch(e: Exception){
//            e.printStackTrace()
        }
    }

}