package com.photos.kilopixels.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by rahul on 19/1/18.
 */
class PhotoDetail() : Parcelable {
    @SerializedName("isfamily") var isfamily: String? = null

    @SerializedName("width_l") var width_l: String? = null

    @SerializedName("width_n") var width_n: String? = null

    @SerializedName("ispublic") var ispublic: String? = null

    @SerializedName("width_m") var width_m: String? = null

    @SerializedName("width_o") var width_o: String? = null

    @SerializedName("width_q") var width_q: String? = null

    @SerializedName("height_sq") var height_sq: String? = null

    @SerializedName("width_t") var width_t: String? = null

    @SerializedName("url_sq") var url_sq: String? = null

    @SerializedName("width_s") var width_s: String? = null

    @SerializedName("isfriend") var isfriend: String? = null

    @SerializedName("height_z") var height_z: String? = null

    @SerializedName("farm") var farm: String? = null

    @SerializedName("id") var id: String? = null

    @SerializedName("title") var title: String? = null

    @SerializedName("url_c") var url_c: String? = null

    @SerializedName("url_o") var url_o: String? = null

    @SerializedName("url_n") var url_n: String? = null

    @SerializedName("width_c") var width_c: String? = null

    @SerializedName("url_m") var url_m: String? = null

    @SerializedName("url_l") var url_l: String? = null

    @SerializedName("url_t") var url_t: String? = null

    @SerializedName("height_m") var height_m: String? = null

    @SerializedName("height_l") var height_l: String? = null

    @SerializedName("url_q") var url_q: String? = null

    @SerializedName("height_o") var height_o: String? = null

    @SerializedName("height_n") var height_n: String? = null

    @SerializedName("url_s") var url_s: String? = null

    @SerializedName("height_q") var height_q: String? = null

    @SerializedName("height_s") var height_s: String? = null

    @SerializedName("height_t") var height_t: String? = null

    @SerializedName("url_z") var url_z: String? = null

    @SerializedName("width_z") var width_z: String? = null

    @SerializedName("owner") var owner: String? = null

    @SerializedName("width_sq") var width_sq: String? = null

    @SerializedName("secret") var secret: String? = null

    @SerializedName("server") var server: String? = null

    @SerializedName("height_c") var height_c: String? = null

    @SerializedName("local_url") var localUrl: String? = null

    @SerializedName("local_uri") var localUri: String? = null

    @SerializedName("page") var pageNumber: String? = null

    @SerializedName("local_saved") var isSavedLocally: Boolean = false

    @SerializedName("item_id") var itemId: Long = 0

    constructor(parcel: Parcel) : this() {
        isfamily = parcel.readString()
        width_l = parcel.readString()
        width_n = parcel.readString()
        ispublic = parcel.readString()
        width_m = parcel.readString()
        width_o = parcel.readString()
        width_q = parcel.readString()
        height_sq = parcel.readString()
        width_t = parcel.readString()
        url_sq = parcel.readString()
        width_s = parcel.readString()
        isfriend = parcel.readString()
        height_z = parcel.readString()
        farm = parcel.readString()
        id = parcel.readString()
        title = parcel.readString()
        url_c = parcel.readString()
        url_o = parcel.readString()
        url_n = parcel.readString()
        width_c = parcel.readString()
        url_m = parcel.readString()
        url_l = parcel.readString()
        url_t = parcel.readString()
        height_m = parcel.readString()
        height_l = parcel.readString()
        url_q = parcel.readString()
        height_o = parcel.readString()
        height_n = parcel.readString()
        url_s = parcel.readString()
        height_q = parcel.readString()
        height_s = parcel.readString()
        height_t = parcel.readString()
        url_z = parcel.readString()
        width_z = parcel.readString()
        owner = parcel.readString()
        width_sq = parcel.readString()
        secret = parcel.readString()
        server = parcel.readString()
        height_c = parcel.readString()
        localUrl = parcel.readString()
        localUri = parcel.readString()
        pageNumber = parcel.readString()
        isSavedLocally = parcel.readByte() != 0.toByte()
        itemId = parcel.readLong()
    }

    override fun toString(): String {
        return "ClassPojo [isfamily = $isfamily, width_l = $width_l, width_n = $width_n, ispublic = $ispublic, width_m = $width_m, width_o = $width_o, width_q = $width_q, height_sq = $height_sq, width_t = $width_t, url_sq = $url_sq, width_s = $width_s, isfriend = $isfriend, height_z = $height_z, farm = $farm, id = $id, title = $title, url_c = $url_c, url_o = $url_o, url_n = $url_n, width_c = $width_c, url_m = $url_m, url_l = $url_l, url_t = $url_t, height_m = $height_m, height_l = $height_l, url_q = $url_q, height_o = $height_o, height_n = $height_n, url_s = $url_s, height_q = $height_q, height_s = $height_s, height_t = $height_t, url_z = $url_z, width_z = $width_z, owner = $owner, width_sq = $width_sq, secret = $secret, server = $server, height_c = $height_c, local_url = $localUrl" +
                ", isSavedLocally = $isSavedLocally, localUri = $localUri, pageNumber = $pageNumber, itemId = $itemId]"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(isfamily)
        parcel.writeString(width_l)
        parcel.writeString(width_n)
        parcel.writeString(ispublic)
        parcel.writeString(width_m)
        parcel.writeString(width_o)
        parcel.writeString(width_q)
        parcel.writeString(height_sq)
        parcel.writeString(width_t)
        parcel.writeString(url_sq)
        parcel.writeString(width_s)
        parcel.writeString(isfriend)
        parcel.writeString(height_z)
        parcel.writeString(farm)
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(url_c)
        parcel.writeString(url_o)
        parcel.writeString(url_n)
        parcel.writeString(width_c)
        parcel.writeString(url_m)
        parcel.writeString(url_l)
        parcel.writeString(url_t)
        parcel.writeString(height_m)
        parcel.writeString(height_l)
        parcel.writeString(url_q)
        parcel.writeString(height_o)
        parcel.writeString(height_n)
        parcel.writeString(url_s)
        parcel.writeString(height_q)
        parcel.writeString(height_s)
        parcel.writeString(height_t)
        parcel.writeString(url_z)
        parcel.writeString(width_z)
        parcel.writeString(owner)
        parcel.writeString(width_sq)
        parcel.writeString(secret)
        parcel.writeString(server)
        parcel.writeString(height_c)
        parcel.writeString(localUrl)
        parcel.writeString(localUri)
        parcel.writeString(pageNumber)
        parcel.writeByte(if (isSavedLocally) 1 else 0)
        parcel.writeLong(itemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PhotoDetail> {
        override fun createFromParcel(parcel: Parcel): PhotoDetail {
            return PhotoDetail(parcel)
        }

        override fun newArray(size: Int): Array<PhotoDetail?> {
            return arrayOfNulls(size)
        }
    }
}