package com.socket9.thetsl.models

import com.google.gson.annotations.SerializedName
import com.socket9.thetsl.utils.SharePref
import nz.bradcampbell.paperparcel.PaperParcel
import nz.bradcampbell.paperparcel.PaperParcelable

/**
 * Created by Euro (ripzery@gmail.com) on 4/11/16 AD.
 */

object Model {
    @PaperParcel
    data class TokenEntity(val token: String) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(TokenEntity::class.java)
        }
    }

    @PaperParcel
    data class BaseModel(val message: String, val result: Boolean) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(BaseModel::class.java)
        }
    }

    @PaperParcel
    data class User(val message: String? = null, val result: Boolean, val data: TokenEntity) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(User::class.java)
        }
    }

    @PaperParcel
    data class Profile(val message: String? = null, val result: Boolean, val data: ProfileEntity? = null) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(Profile::class.java)
        }
    }

    @PaperParcel
    data class Contact(val message: String? = null, val result: Boolean, val data: ContactEntity) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(Contact::class.java)
        }
    }

    data class ListContacts(val message: String? = null, val result: Boolean, val data: MutableList<ContactEntity>)

    data class ListNewsEvent(val message: String? = null, val result: Boolean, val data: MutableList<NewsEventEntity>)

    data class NewsEvent(val message: String? = null, val result: Boolean, val data: NewsEventEntity)

    data class Photo(val message: String? = null, val result: Boolean,
                     val data: PhotoEntity)

    data class PhotoEntity(val pathUse: String, val pathSave: String)

    @PaperParcel
    data class ProfileEntity(val nameTh: String,
                             val nameEn: String,
                             val phone: String,
                             val password: String? = null,
                             val address: String ? = null,
                             val email: String,
                             val pic: String? = null,
                             val facebookPic: String? = null) : PaperParcelable {

        fun getName(): String {
            return if (SharePref.isEnglish()) nameEn else nameTh
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ProfileEntity::class.java)
        }
    }

    @PaperParcel
    data class NewsEventEntity(val id: Int,
                               val titleTh: String,
                               val titleEn: String,
                               val pic: String? = null,
                               val type: String,
                               val date: String,
                               val contentEn: String,
                               val contentTh: String) {

        fun getTitle(): String {
            return if (SharePref.isEnglish()) titleEn else titleTh
        }

        fun getContent(): String {
            return if (SharePref.isEnglish()) contentEn else contentTh
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(NewsEventEntity::class.java)
        }
    }

    @PaperParcel
    data class ContactEntity(val id: Int,
                             val titleEn: String,
                             val subTitle: String? = null,
                             val icon: Int,
                             val titleTh: String? = null,
                             @SerializedName("Phone") val phone: String? = null,
                             @SerializedName("Address") val address: String? = null,
                             @SerializedName("Fax") val fax: String? = null,
                             @SerializedName("Email") val email: String? = null,
                             @SerializedName("Business Hours") val businessHours: String? = null,
                             val lng: Double? = null,
                             val lat: Double? = null) : PaperParcelable {


        fun getTitle(): String {
            return if (SharePref.isEnglish()) titleEn else titleTh ?: titleEn
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ContactEntity::class.java)
        }
    }

    data class BasicData(val id: Int, val nameEn: String, val nameTh: String) {
        fun getName(): String {
            return if (SharePref.isEnglish()) nameEn else nameTh
        }
    }

    data class BrandServiceData(val name: String, var modelServices: MutableList<String>)

    data class BasicDataList(val branches: MutableList<BasicData>,
                             val serviceTypes: MutableList<BasicData>,
                             val brandServices: MutableList<BrandServiceData>)

    data class ServiceBasicData(val result: Boolean, val message: String? = null, val data: BasicDataList)

    data class NewBooking(val licensePlate: String,
                          val modelService: String,
                          val brandService: String,
                          val dateBooking: String,
                          val serviceTypes: Int,
                          val branchesid: Int,
                          val note: String,
                          val phone: String)

    @PaperParcel
    data class ServiceBookingEntity(val licensePlate: String,
                                    val dateBooking: String,
                                    val modelCategoryTh: String,
                                    val modelCategoryEn: String,
                                    val serviceTypeTh: String,
                                    val serviceTypeEn: String,
                                    val note: String,
                                    val phone: String,
                                    val branchesTh: String,
                                    val branchesEn: String,
                                    val isCancel: Boolean,
                                    val dateConfirm: String) : PaperParcelable {

        fun getModelCategory(): String {
            return if (SharePref.isEnglish()) modelCategoryEn else modelCategoryTh
        }

        fun getBranch(): String {
            return if (SharePref.isEnglish()) branchesEn else branchesTh
        }

        fun getService(): String {
            return if (SharePref.isEnglish()) serviceTypeEn else serviceTypeTh
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceBookingEntity::class.java)
        }

    }

    @PaperParcel
    data class ServiceTrackingStatus(val dateFinish: String, val dateReceive: String, val iconId: Int, val statusTh: String, val statusEn: String) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceTrackingStatus::class.java)
        }

        fun getStatus(): String {
            return if (SharePref.isEnglish()) statusEn else statusTh
        }

    }

    @PaperParcel
    data class ServiceTrackingEntity(val detail: MutableList<ServiceTrackingStatus>,
                                     val licensePlate: String,
                                     val model: String,
                                     val serviceJobNumber: String,
                                     val trackingid: String) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceTrackingEntity::class.java)
        }

    }

    data class ServiceBookingList(val result: Boolean, val message: String? = null, val data: MutableList<ServiceBookingEntity>)

    data class ServiceTrackingList(val result: Boolean, val message: String? = null, val data: MutableList<ServiceTrackingEntity>)
}