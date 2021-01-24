package com.like.paging.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class TopArticleEntity {

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    override fun toString(): String {
        return "TopArticleEntity(id=$id, title=$title)"
    }
}