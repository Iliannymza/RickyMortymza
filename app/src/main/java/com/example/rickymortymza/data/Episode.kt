package com.example.rickymortymza.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EpisodeResponse(
    @SerializedName("info") val info: Info,
    @SerializedName("results") val results: List<Episode>
)

data class Episode(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("air_date") val air_date: String, // Fecha de emision
    @SerializedName("episode") val episode: String, // Ejemplo: "S01E01"
    @SerializedName("url") val url: String,
    @SerializedName("created") val created: String,
    @SerializedName("characters") val characters: List<String> = emptyList()
) : Serializable {
    companion object {
        const val TABLE_NAME = "episodes"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_NAME = "name"
        const val COLUMN_NAME_AIR_DATE = "air_date"
        const val COLUMN_NAME_EPISODE_CODE = "episode_code" // Cambio de nombre para claridad
        const val COLUMN_NAME_URL = "url"
        const val COLUMN_NAME_CREATED = "created"
    }
}
