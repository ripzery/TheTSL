package com.socket9.thetsl.model

import com.google.gson.annotations.SerializedName
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
    data class User(val message: String, val result: Boolean, val data: TokenEntity) : PaperParcelable {
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

    data class Contact(val message: String? = null, val result: Boolean, val data: ContactEntity)

    data class ListContacts(val message:String? = null, val result: Boolean, val data: MutableList<ContactEntity>)

    data class ListNewsEvent(val message:String? = null, val result: Boolean, val data: MutableList<NewsEventEntity>)

    data class NewsEvent(val message:String? = null, val result: Boolean, val data:NewsEventEntity)

    data class Photo(val message:String? = null, val result: Boolean,
                     val data: PhotoEntity)

    data class PhotoEntity(val pathUse:String, val pathSave: String)

    @PaperParcel
    data class ProfileEntity(val nameTh: String,
                             val nameEn: String,
                             val phone: String,
                             val address: String,
                             val email: String,
                             val pic: String? = null,
                             val facebookPic: String) : PaperParcelable {
        companion object {
            @JvmField val CREATOR = PaperParcelable.Creator(ProfileEntity::class.java)
        }
    }

    data class NewsEventEntity(val id: Int,
                               val titleTh: String,
                               val titleEn: String,
                               val pic: String? = null,
                               val type: String,
                               val date: String,
                               val contentEn: String,
                               val contentTh: String)

    data class ContactEntity(val id: Int,
                             @SerializedName("Phone") val phone: String,
                             val titleTh: String,
                             val titleEn: String,
                             val subTitle: String? = null,
                             @SerializedName("Address") val address: String,
                             @SerializedName("Fax") val fax: String,
                             @SerializedName("Email") val email: String,
                             @SerializedName("Business Hours") val businessHours: String,
                             val lng: Double,
                             val lat: Double,
                             val icon: Int)
}