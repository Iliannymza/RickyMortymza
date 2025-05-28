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

    fun insert(episode: Episode) {
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

    fun update(episode: Episode) {
        open()
        try {
            val values = ContentValues().apply {
                put(Episode.COLUMN_NAME_NAME, episode.name)
                put(Episode.COLUMN_NAME_AIR_DATE, episode.air_date)
                put(Episode.COLUMN_NAME_EPISODE_CODE, episode.episode)
                put(Episode.COLUMN_NAME_URL, episode.url)
                put(Episode.COLUMN_NAME_CREATED, episode.created)
            }

            val selection = "${Episode.COLUMN_NAME_ID}"


            val count = db.update(Episode.TABLE_NAME, values, selection, null)

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
            val selection = "${Episode.COLUMN_NAME_ID}"
            val deletedRows = db.delete(Episode.TABLE_NAME, selection, null)
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
                Episode.COLUMN_NAME_URL,
                Episode.COLUMN_NAME_CREATED
            )

            val selection = "${Episode.COLUMN_NAME_ID} = ?"

            val cursor = db.query(
                Episode.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_NAME))
                val airDate = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_AIR_DATE))
                val episodeCode = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_EPISODE_CODE))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_URL))
                val created = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CREATED))

                episode = Episode(localId, name, airDate, episodeCode, url, created)
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

                val episode = Episode(localId, name, airDate, episodeCode, url, created)
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

    fun findAllByName(query: String): List<Episode> {
        open()
        val episodeList: MutableList<Episode> = mutableListOf()
        try {
            val projection = arrayOf(
                Episode.COLUMN_NAME_ID,
                Episode.COLUMN_NAME_NAME,
                Episode.COLUMN_NAME_AIR_DATE,
                Episode.COLUMN_NAME_EPISODE_CODE,
                Episode.COLUMN_NAME_URL,
                Episode.COLUMN_NAME_CREATED
            )

            val selection =
                "${Episode.COLUMN_NAME_NAME} LIKE '%$query%'"

            val cursor = db.query(
                Episode.TABLE_NAME,
                projection,
                selection, null, null, null, null
            )

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_NAME))
                val airDate = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_AIR_DATE))
                val episodeCode = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_EPISODE_CODE))
                val url = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_URL))
                val created = cursor.getString(cursor.getColumnIndexOrThrow(Episode.COLUMN_NAME_CREATED))

                val episode = Episode(localId, name, airDate, episodeCode, url, created)
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
