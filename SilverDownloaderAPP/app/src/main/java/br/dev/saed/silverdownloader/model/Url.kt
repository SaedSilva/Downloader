package br.dev.saed.silverdownloader.model

import com.google.gson.annotations.SerializedName

data class Url(
    val code: Int,
    val id: Int,
    val name: String,
    @SerializedName("url")
    val link: String
)