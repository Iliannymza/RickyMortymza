package com.example.rickymortymza.data

import java.io.Serializable

data class CharactersEpisodes(
    val characterId: Long,
    val episodeId: Long
) : Serializable{
    companion object {
        const val TABLE_NAME = "character_episode"

        const val COLUMN_NAME_CHARACTER_ID = "character_id"
        const val COLUMN_NAME_EPISODE_ID = "episode_id"
    }
}