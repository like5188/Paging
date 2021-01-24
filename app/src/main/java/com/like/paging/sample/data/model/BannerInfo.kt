package com.like.paging.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * @Entity的详细文档：https://www.jianshu.com/p/66a586a6fbe0
 */
class BannerInfo {

    var bannerEntities: List<BannerEntity>? = null

    @Entity
    class BannerEntity {
        @PrimaryKey
        @SerializedName("id")
        var id: Int? = null

        @SerializedName("imagePath")
        var imagePath: String? = null

        override fun toString(): String {
            return "BannerEntity(id=$id)"
        }
    }

    override fun toString(): String {
        return "BannerInfo(bannerEntities=$bannerEntities)"
    }

}