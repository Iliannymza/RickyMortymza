package com.example.rickymortymza.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.rickymortymza.utils.DatabaseManager

class EpisodeDao(private val context: Context) {
    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    fun insert(character: Character) {
        open()

        try {
            val values = ContentValues().apply {
                put(Episode.COLUMN_NAME_ID, episode.id)
                put(Episode.COLUMN_NAME_NAME, episode.name)
                put(Episode.COLUMN_NAME_AIR_DATE, episode.air_date)
                put(Episode.COLUMN_NAME_EPISODE_CODE, episode.episode) // Usamos 'episode' del objeto
                put(Episode.COLUMN_NAME_URL, episode.url)
                put(Episode.COLUMN_NAME_CREATED, episode.created)
        }
            val newRowId = db.insert(Episode.TABLE_NAME, null, values)
            Log.i("DATABASE", "Inserted an episode with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }



    }
}


/*
fun insertAll(episodes: List<Episode>) {
        open()
        db.beginTransaction() // Inicia una transacción para inserciones masivas
        try {
            for (episode in episodes) {
                val values = ContentValues().apply {
                    put(Episode.COLUMN_NAME_ID, episode.id)
                    put(Episode.COLUMN_NAME_NAME, episode.name)
                    put(Episode.COLUMN_NAME_AIR_DATE, episode.air_date)
                    put(Episode.COLUMN_NAME_EPISODE_CODE, episode.episode)
                    put(Episode.COLUMN_NAME_URL, episode.url)
                    put(Episode.COLUMN_NAME_CREATED, episode.created)
                    put(Episode.COLUMN_NAME_CHARACTERS, gson.toJson(episode.characters))
                }
                db.insertWithOnConflict(Episode.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE)
            }
            db.setTransactionSuccessful() // Marca la transacción como exitosa
            Log.i("DATABASE", "Inserted all episodes successfully.")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction() // Finaliza la transacción
            close()
        }
    }

    fun update(episode: Episode) {
        open()
        try {
            val values = ContentValues().apply {
                put(Episode.COLUMN_NAME_NAME, episode.name)
                put(Episode.COLUMN_NAME_AIR_DATE, episode.air_date)
                put(Episode.COLUMN_NAME_EPISODE_CODE, episode.episode)
                put(Episode.COLUMN_NAME_URL, episode.url)
                put(Episode.COLUMN_NAME_CREATED, episode.created)
                put(Episode.COLUMN_NAME_CHARACTERS, gson.toJson(episode.characters))
            }

            val selection = "${Episode.COLUMN_NAME_ID} = ?"
            val selectionArgs = arrayOf(episode.id.toString())

            val count = db.update(Episode.TABLE_NAME, values, selection, selectionArgs)
            Log.i("DATABASE", "Updated $count rows for episode with id: ${episode.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun delete(episode: Episode) {
        open()
        try {
            val selection = "${Episode.COLUMN_NAME_ID} = ?"
            val selectionArgs = arrayOf(episode.id.toString())
            val deletedRows = db.delete(Episode.TABLE_NAME, selection, selectionArgs)
            Log.i("DATABASE", "Deleted $deletedRows rows for episode with id: ${episode.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun deleteAll() {
        open()
        try {
            val deletedRows = db.delete(Episode.TABLE_NAME, null, null)
            Log.i("DATABASE", "Deleted all episodes: $deletedRows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    fun findById(id: Long): Episode? {
        open()
        var episode: Episode? = null
        try {
            val projection = arrayOf(
                Episode.COLUMN_NAME_ID,
                Episode.COLUMN_NAME_NAME,
                Episode.COLUMN_NAME_AIR_DATE,
                Episode.COLUMN_NAME_EPISODE_CODE,
                Episode.COLUMN_NAME_CHARACTERS,
                Episode.COLUMN_NAME_URL,
                Episode.COLUMN_NAME_CREATED
            )

            val selection = "${Episode.COLUMN_NAME_ID} = ?"
            val selectionArgs = arrayOf(id.toString())

            val cursor = db.query(
                Episode.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null, null, null
            )

            if (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_NAME))
                val airDate = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_AIR_DATE))
                val episodeCode = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_EPISODE_CODE))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_URL))
                val created = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CREATED))

                // Recupera el JSON String de los personajes y convierte a List<String>
                val charactersJson = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CHARACTERS))
                val charactersType = object : TypeToken<List<String>>() {}.type
                val characters: List<String> = gson.fromJson(charactersJson, charactersType) ?: emptyList()

                episode = Episode(localId, name, airDate, episodeCode, characters, url, created)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return episode
    }

    fun findAll(): List<Episode> {
        open()
        val episodeList: MutableList<Episode> = mutableListOf()
        try {
            val projection = arrayOf(
                Episode.COLUMN_NAME_ID,
                Episode.COLUMN_NAME_NAME,
                Episode.COLUMN_NAME_AIR_DATE,
                Episode.COLUMN_NAME_EPISODE_CODE,
                Episode.COLUMN_NAME_CHARACTERS,
                Episode.COLUMN_NAME_URL,
                Episode.COLUMN_NAME_CREATED
            )

            val cursor = db.query(
                Episode.TABLE_NAME,
                projection,
                null, null, null, null, null
            )

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_NAME))
                val airDate = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_AIR_DATE))
                val episodeCode = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_EPISODE_CODE))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_URL))
                val created = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CREATED))

                val charactersJson = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CHARACTERS))
                val charactersType = object : TypeToken<List<String>>() {}.type
                val characters: List<String> = gson.fromJson(charactersJson, charactersType) ?: emptyList()

                val episode = Episode(localId, name, airDate, episodeCode, characters, url, created)
                episodeList.add(episode)
            }
            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
        return episodeList
    }
}
 */