package com.like.paging.sample.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
class ArticleEntity {

    @PrimaryKey
    @SerializedName("id")
    var id: Int? = null

    @SerializedName("title")
    var title: String? = null

    override fun toString(): String {
        return "ArticleEntity(id=$id, title=$title)"
    }

}