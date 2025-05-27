package com.example.rickymortymza.data

import com.google.gson.annotations.SerializedName

data class CharacterResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Character>
)

data class Info(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val prev: String?
)
// Para personajes individuales
data class Character(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: String,
    @SerializedName("species") val species: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("image") val image: String
) {
    companion object {
        const val TABLE_NAME = "Characters"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_STATUS = "status"
        const val COLUMN_NAME_SPECIES = "species"
        const val COLUMN_NAME_GENDER = "gender"
        const val COLUMN_NAME_IMAGE = "image"
    }
}

//para las ubicaciones (origin y location)
data class Location(
    @SerializedName("name") val name: String,
    @SerializedName("url") val url: String
)

/*data class CharactersTable (
    val id: Long,
    val apiid: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String
)   {
    companion object {
        const val TABLE_NAME = "Characters"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_APIID = "apiid"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_STATUS = "status"
        const val COLUMN_NAME_SPECIES = "species"
        const val COLUMN_NAME_GENDER = "gender"
        const val COLUMN_NAME_IMAGE = "image"
    }
}*/