package com.socket9.thetsl.network

import com.socket9.thetsl.models.Model
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import rx.Observable

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */

object ApiService {

    //    val BASE_URL = "http://tsl.socket9.com/api/";
    var retrofit: Retrofit? = null
    //    val BASE_URL = "http://192.168.100.31:91/api/"
    val BASE_URL = "http://www.tsl.co.th/api/"

    fun getAPI(): TSLApi {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build()
        }

        val apiService: TSLApi = retrofit!!.create(TSLApi::class.java)

        return apiService
    }

    interface TSLApi {
        @FormUrlEncoded
        @POST("checkLogin")
        fun login(@Field("email") email: String, @Field("password") password: String, @Field("deviceid") deviceId: String): Observable<Model.User>

        @FormUrlEncoded
        @POST("checkLogin")
        fun loginWithFb(@Field("facebookid") facebookId: String, @Field("facebookpic") facebookpic: String, @Field("deviceid") deviceId: String): Observable<Model.User>

        @FormUrlEncoded
        @POST("forgetPassword")
        fun forgetPassword(@Field("email") email: String): Observable<Model.BaseModel>

        @FormUrlEncoded
        @POST("registerUser")
        fun registerUser(@Field("username") username: String,
                         @Field("password") password: String,
                         @Field("nameEn") nameEn: String,
                         @Field("nameTh") nameTh: String,
                         @Field("email") email: String,
                         @Field("address") address: String,
                         @Field("phone") phone: String,
                         @Field("facebookid") facebookId: String,
                         @Field("facebookpic") facebookPic: String): Observable<Model.User>

        @FormUrlEncoded
        @POST("getProfile")
        fun getProfile(@Field("token") token: String): Observable<Model.Profile>

        @FormUrlEncoded
        @POST("getListNews")
        fun getListNews(@Field("token") token: String): Observable<Model.ListNewsEvent>

        @FormUrlEncoded
        @POST("getNew")
        fun getNews(@Field("token") token: String, @Field("newid") newId: Int): Observable<Model.NewsEvent>

        @FormUrlEncoded
        @POST("getListEvents")
        fun getListEvents(@Field("token") token: String): Observable<Model.ListNewsEvent>

        @FormUrlEncoded
        @POST("getEvent")
        fun getEvent(@Field("token") token: String, @Field("eventid") id: Int): Observable<Model.NewsEvent>

        @FormUrlEncoded
        @POST("getListContacts")
        fun getListContacts(@Field("token") token: String): Observable<Model.ListContacts>

        @FormUrlEncoded
        @POST("getContact")
        fun getContact(@Field("token") token: String, @Field("contactid") contactId: Int): Observable<Model.Contact>

        @FormUrlEncoded
        @POST("emergencyCall")
        fun emergencyCall(@Field("token") token: String, @Field("lat") lat: String, @Field("lng") lng: String, @Field("type") type: String): Observable<Model.BaseModel>

        @FormUrlEncoded
        @POST("uploadPhotoBase64")
        fun uploadPhoto(@Field("token") token: String, @Field("photo") path: String): Observable<Model.Photo>

        @FormUrlEncoded
        @POST("updateProfile")
        fun updateProfile(@Field("token") token: String,
                          @Field("nameEn") nameEn: String,
                          @Field("nameTh") nameTh: String,
                          @Field("password") password: String,
                          @Field("phone") phone: String,
                          @Field("address") address: String,
                          @Field("picture") picture: String): Observable<Model.BaseModel>

        @FormUrlEncoded
        @POST("updateProfile")
        fun updatePicture(@Field("token") token: String,
                          @Field("picture") picture: String): Observable<Model.BaseModel>

        @FormUrlEncoded
        @POST("getServiceBasicData")
        fun getServiceBasicData(@Field("token") token: String): Observable<Model.ServiceBasicData>

        @FormUrlEncoded
        @POST("serviceBooking")
        fun bookService(@Field("token") token: String,
                        @Field("licensePlate") licensePlate: String,
                        @Field("dateBooking") dateBooking: String,
                        @Field("modelService") modelService: String,
                        @Field("brandService") brandService: String,
                        @Field("serviceTypes") serviceTypes: Int,
                        @Field("note") note: String,
                        @Field("phone") phone: String,
                        @Field("branchesid") branchesid: Int): Observable<Model.BaseModel>

        @FormUrlEncoded
        @POST("getServiceBookingByUser")
        fun getServiceBookingList(@Field("token") token: String,
                                  @Field("order") order: String): Observable<Model.ServiceBookingList>

        @FormUrlEncoded
        @POST("getServiceTrackingByUser")
        fun getServiceTrackingList(@Field("token") token: String,
                                   @Field("order") order: String): Observable<Model.ServiceTrackingList>

        @FormUrlEncoded
        @POST("serviceCarTracking")
        fun serviceCarTracking(@Field("token") token: String,
                               @Field("serviceJobNumber") order: String,
                               @Field("trackingid") trackingId: String): Observable<Model.ServiceTrackingList>

        @FormUrlEncoded
        @POST("saveDeviceByToken")
        fun saveDeviceByToken(@Field("token") token: String, @Field("deviceid") deviceId: String): Observable<Model.BaseModel>
    }

}