package com.example.rickymortymza.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.rickymortymza.utils.DatabaseManager

class CharactersEpisodesDao(private val context: Context) {
    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        if (this::db.isInitialized && db.isOpen) {
            db.close()
        }
    }

    fun insert(charactersEpisodes: CharactersEpisodes) {
        open()
        try {
            val values = ContentValues().apply {
                put(CharactersEpisodes.COLUMN_NAME_CHARACTER_ID, charactersEpisodes.characterId)
                put(CharactersEpisodes.COLUMN_NAME_EPISODE_ID, charactersEpisodes.episodeId)
            }
            val newRowId = db.insert(CharactersEpisodes.TABLE_NAME, null, values)
            if (newRowId != -1L) {
                Log.i("CharactersEpisodesDao", "Inserted character-episode link: $newRowId")
            } else {
                Log.w("CharactersEpisodesDao", "No se pudo insertar el enlace del personaje al episodio o ya existe: CharID=${charactersEpisodes.characterId}, EpID=${charactersEpisodes.episodeId}")
            }
        } catch (e: Exception) {
            Log.e("CharactersEpisodesDao", "Error inserting character-episode link: ${e.message}", e)
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun deleteAll() {
        open()
        try {
            val deletedRows = db.delete(CharactersEpisodes.TABLE_NAME, null, null)
            Log.i("CharactersEpisodesDao", ": $deletedRows")
        } catch (e: Exception) {
            Log.e("CharactersEpisodesDao", "Error al eliminar todos los enlaces entre personajes y episodios: ${e.message}", e)
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun findEpisodeIdsByCharacterId(characterId: Long): List<Long> {
        open()
        val episodeIds = mutableListOf<Long>()
        try {
            val projection = arrayOf(CharactersEpisodes.COLUMN_NAME_EPISODE_ID)
            val selection = "${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID} = ?"
            val selectionArgs = arrayOf(characterId.toString())

            val cursor = db.query(
                CharactersEpisodes.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val episodeId = cursor.getLong(cursor.getColumnIndexOrThrow(CharactersEpisodes.COLUMN_NAME_EPISODE_ID))
                episodeIds.add(episodeId)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CharactersEpisodesDao", "Error al encontrar los ID de episodios por personaje ID: ${e.message}", e)
            e.printStackTrace()
        } finally {
            close()
        }
        return episodeIds
    }

    fun findCharacterIdsByEpisodeId(episodeId: Long): List<Long> {
        open()
        val characterIds = mutableListOf<Long>()
        try {
            val projection = arrayOf(CharactersEpisodes.COLUMN_NAME_CHARACTER_ID)
            val selection = "${CharactersEpisodes.COLUMN_NAME_EPISODE_ID} = ?"
            val selectionArgs = arrayOf(episodeId.toString())

            val cursor = db.query(
                CharactersEpisodes.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val characterId = cursor.getLong(cursor.getColumnIndexOrThrow(CharactersEpisodes.COLUMN_NAME_CHARACTER_ID))
                characterIds.add(characterId)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("CharactersEpisodesDao", "Error al encontrar los ID de los personajes por episodio ID: ${e.message}", e)
            e.printStackTrace()
        } finally {
            close()
        }
        return characterIds
    }
}