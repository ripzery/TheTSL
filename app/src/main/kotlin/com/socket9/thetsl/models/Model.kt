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
                             var phone: String,
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
                               val titleTh: String? = null,
                               val titleEn: String,
                               val pic: String? = null,
                               val type: String? = null,
                               val date: String,
                               val contentEn: String,
                               val contentTh: String? = null) : PaperParcelable {

        fun getTitle(): String {
            return if (SharePref.isEnglish()) titleEn else {
                titleTh ?: titleEn
            }
        }

        fun getContent(): String {
            return if (SharePref.isEnglish()) contentEn else {
                contentTh ?: contentEn
            }
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(NewsEventEntity::class.java)
        }
    }

    @PaperParcel data class ContactEntity(val id: Int,
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

    @PaperParcel data class BasicData(val id: Int, val nameEn: String, val nameTh: String) : PaperParcelable {
        fun getName(): String {
            return if (SharePref.isEnglish()) nameEn else nameTh
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(BasicData::class.java)
        }
    }

    @PaperParcel data class BrandServiceData(val name: String, var modelServices: MutableList<String>) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(BrandServiceData::class.java)
        }
    }

    @PaperParcel data class BasicDataList(val branches: MutableList<BasicData>,
                                          val serviceTypes: MutableList<BasicData>,
                                          val brandServices: MutableList<BrandServiceData>) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(BasicDataList::class.java)
        }
    }

    @PaperParcel data class ServiceBasicData(val result: Boolean, val message: String? = null, val data: BasicDataList) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceBasicData::class.java)
        }
    }

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
                                    val modelCategoryTh: String? = null,
                                    val modelCategoryEn: String? = null,
                                    val modelService: String,
                                    val serviceTypeTh: String,
                                    val serviceTypeEn: String,
                                    val note: String,
                                    val image: String,
                                    val phone: String,
                                    val branchesTh: String,
                                    val branchesEn: String,
                                    val isCancel: Boolean,
                                    val dateConfirm: String? = null) : PaperParcelable {

        fun getBranch(): String {
            return if (SharePref.isEnglish()) branchesEn else branchesTh
        }

        fun getService(): String {
            return if (SharePref.isEnglish()) serviceTypeEn else serviceTypeTh
        }

        fun getDate(): String {
            val dateFormat = if(dateConfirm.isNullOrEmpty()) dateBooking else dateConfirm
            return dateFormat!!.split(" ")[0]
        }

        fun getTime(): String {
            var dateFormat = if(dateConfirm.isNullOrEmpty()) dateBooking else dateConfirm
            dateFormat = dateFormat!!.substring(0, dateFormat.length - 3)
            return dateFormat.split(" ")[1]
        }

        fun getDateTime(): String {
            val dateFormat = if(dateConfirm.isNullOrEmpty()) dateBooking else dateConfirm
            return dateFormat!!.substring(0, dateFormat.length-3)
        }

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceBookingEntity::class.java)
        }

    }

    @PaperParcel
    data class ServiceTrackingStatus(val dateFinish: String, val dateReceive: String, val iconid: Int, val statusTh: String, val statusEn: String) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceTrackingStatus::class.java)
        }

        fun getStatus(): String {
            return "${if (SharePref.isEnglish()) {
                statusEn
            } else {
                statusTh
            } }"
        }

    }

    @PaperParcel
    data class ServiceTrackingEntity(val detail: MutableList<ServiceTrackingStatus>,
                                     val licensePlate: String,
                                     val model: String,
                                     val image: String? = null,
                                     val serviceJobNumber: String,
                                     val serviceTypeEn: String? = null,
                                     val serviceTypeTh: String? = null,
                                     val trackingid: String) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ServiceTrackingEntity::class.java)
        }

        fun getServiceType(): String? {
            return "${if (SharePref.isEnglish()) serviceTypeEn else serviceTypeTh}"
        }

    }

    @PaperParcel
    data class CarTrackingEntity(val detail: MutableList<ServiceTrackingStatus>,
                                 val licensePlate: String,
                                 val model: String,
                                 val image: String? = null,
                                 val preemption: String,
                                 val idCard: String? = null) : PaperParcelable {

        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(CarTrackingEntity::class.java)
        }

    }

    data class CarTrackingSaveList(val result: Boolean, val message: String? = null, val data: CarTrackingEntity)

    data class CarTrackingList(val result: Boolean, val message: String? = null, val data: MutableList<CarTrackingEntity>)

    data class ServiceBookingList(val result: Boolean, val message: String? = null, val data: MutableList<ServiceBookingEntity>)

    data class ServiceTrackingList(val result: Boolean, val message: String? = null, val data: MutableList<ServiceTrackingEntity>)

    data class ServiceCarTrackingList(val result: Boolean, val message: String? = null, val data: ServiceTrackingEntity)

    @PaperParcel
    data class GCMData(val type: String = "", val statusId: Int = -1, val data: String? = null) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(GCMData::class.java)
        }
    }
}