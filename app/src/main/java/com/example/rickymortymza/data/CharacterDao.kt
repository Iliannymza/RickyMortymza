package com.example.rickymortymza.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.rickymortymza.utils.DatabaseManager

class CharacterDao(private val context: Context) {
    // base de datos seguir a mansour
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
                put(Character.COLUMN_NAME_ID, character.id)
                put(Character.COLUMN_NAME_NAME, character.name)
                put(Character.COLUMN_NAME_STATUS, character.status)
                put(Character.COLUMN_NAME_SPECIES, character.species)
                put(Character.COLUMN_NAME_GENDER, character.gender)
                put(Character.COLUMN_NAME_IMAGE, character.image)
                put(Character.COLUMN_NAME_TYPE, character.type)
                put(Character.COLUMN_NAME_ORIGIN, character.origin.name)
                put(Character.COLUMN_NAME_LOCATION, character.location.name)
                put(Character.COLUMN_NAME_URL, character.url)
            }

            val newRowId = db.insert(Character.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted a character with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Actualizar un personaje
    fun update(character: Character) {
        open()

        try {
            val values = ContentValues().apply {
                put(Character.COLUMN_NAME_ID, character.id)
                put(Character.COLUMN_NAME_NAME, character.name)
                put(Character.COLUMN_NAME_STATUS, character.status)
                put(Character.COLUMN_NAME_SPECIES, character.species)
                put(Character.COLUMN_NAME_GENDER, character.gender)
                put(Character.COLUMN_NAME_IMAGE, character.image)
                put(Character.COLUMN_NAME_IMAGE, character.type)
                put(Character.COLUMN_NAME_ORIGIN, character.origin.name)
                put(Character.COLUMN_NAME_LOCATION, character.location.name)
                put(Character.COLUMN_NAME_URL, character.url)
            }

            val selection = "${Character.COLUMN_NAME_ID} = ${character.id}"

            val count = db.update(Character.TABLE_NAME, values, selection, null)

            Log.i("DATABASE", "Updated character with id: ${character.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar un personaje
    fun delete(character: Character) {
        open()
        try {
            val selection = "${Character.COLUMN_NAME_ID} = ${character.id}"
            val deletedRows = db.delete(Character.TABLE_NAME, selection, null)

            Log.i("DATABASE", "Deleted character with id: ${character.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar un personaje
    fun deleteAll() {
        open()
        try {
            val deletedRows = db.delete(Character.TABLE_NAME, null, null)

            Log.i("DATABASE", "Deleted all character: $deletedRows")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Obtener un registro por ID de la base de datos local
    fun findById(id: Long): Character? {
        open()

        var character: Character? = null
        try {
            val projection = arrayOf(
                Character.COLUMN_NAME_ID,
                Character.COLUMN_NAME_NAME,
                Character.COLUMN_NAME_STATUS,
                Character.COLUMN_NAME_SPECIES,
                Character.COLUMN_NAME_GENDER,
                Character.COLUMN_NAME_IMAGE,
                Character.COLUMN_NAME_TYPE,
                Character.COLUMN_NAME_ORIGIN,
                Character.COLUMN_NAME_LOCATION,
                Character.COLUMN_NAME_URL
            )

            val selection = "${Character.COLUMN_NAME_ID} = $id"

            val cursor = db.query(
                Character.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_NAME))
                val status =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_STATUS))
                val species =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_SPECIES))
                val gender =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_GENDER))
                val image =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_IMAGE))
                val type =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_TYPE))
                val origin =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ORIGIN))
                val location =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_LOCATION))
                val url =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_URL))

                character = Character(
                    localId,
                    name,
                    status,
                    species,
                    gender,
                    image,
                    type,
                    Location(origin),
                    Location(location),
                    url
                )
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return character
    }

    // Obtener todos los registros
    fun findAll(): List<Character> {
        open()

        val characterList: MutableList<Character> = mutableListOf()
        try {
            val projection = arrayOf(
                Character.COLUMN_NAME_ID,
                Character.COLUMN_NAME_NAME,
                Character.COLUMN_NAME_STATUS,
                Character.COLUMN_NAME_SPECIES,
                Character.COLUMN_NAME_GENDER,
                Character.COLUMN_NAME_IMAGE,
                Character.COLUMN_NAME_TYPE,
                Character.COLUMN_NAME_ORIGIN,
                Character.COLUMN_NAME_LOCATION,
                Character.COLUMN_NAME_URL,

                )

            val cursor = db.query(
                Character.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_NAME))
                val status =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_STATUS))
                val species =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_SPECIES))
                val gender =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_GENDER))
                val image =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_IMAGE))
                val type =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_TYPE))
                val origin =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ORIGIN))
                val location =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_LOCATION))
                val url =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_URL))

                val character = Character(
                    localId,
                    name,
                    status,
                    species,
                    gender,
                    image,
                    type,
                    Location(origin),
                    Location(location),
                    url
                )
                characterList.add(character)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return characterList
    }

    // Obtener todos los registros
    fun findAllByEpisodeId(episodeId: Long): List<Character> {
        open()

        val characterList: MutableList<Character> = mutableListOf()
        try {
            val rawQuery = "SELECT * FROM ${Character.TABLE_NAME} INNER JOIN ${CharactersEpisodes.TABLE_NAME} ON ${Character.COLUMN_NAME_ID} = ${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID} WHERE ${CharactersEpisodes.COLUMN_NAME_EPISODE_ID} = $episodeId"


            val cursor = db.rawQuery(rawQuery, null)

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_NAME))
                val status =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_STATUS))
                val species =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_SPECIES))
                val gender =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_GENDER))
                val image =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_IMAGE))
                val type =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_TYPE))
                val origin =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ORIGIN))
                val location =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_LOCATION))
                val url =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_URL))

                val character = Character(
                    localId,
                    name,
                    status,
                    species,
                    gender,
                    image,
                    type,
                    Location(origin),
                    Location(location),
                    url
                )
                characterList.add(character)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return characterList
    }

    // Obtener todos los registros
    fun findAllByName(query: String): List<Character> {
        open()

        val characterList: MutableList<Character> = mutableListOf()
        try {
            val projection = arrayOf(
                Character.COLUMN_NAME_ID,
                Character.COLUMN_NAME_NAME,
                Character.COLUMN_NAME_STATUS,
                Character.COLUMN_NAME_SPECIES,
                Character.COLUMN_NAME_GENDER,
                Character.COLUMN_NAME_IMAGE,
                Character.COLUMN_NAME_TYPE,
                Character.COLUMN_NAME_ORIGIN,
                Character.COLUMN_NAME_LOCATION,
                Character.COLUMN_NAME_URL,
            )

            val selection =
                "${Character.COLUMN_NAME_NAME} LIKE '%$query%' OR ${Character.COLUMN_NAME_SPECIES} LIKE '%$query%'"

            val cursor = db.query(
                Character.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ID))
                val name =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_NAME))
                val status =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_STATUS))
                val species =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_SPECIES))
                val gender =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_GENDER))
                val image =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_IMAGE))
                val type =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_TYPE))
                val origin =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_ORIGIN))
                val location =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_LOCATION))
                val url =
                    cursor.getString(cursor.getColumnIndexOrThrow(Character.COLUMN_NAME_URL))

                val character = Character(
                    localId,
                    name,
                    status,
                    species,
                    gender,
                    image,
                    type,
                    Location(origin),
                    Location(location),
                    url
                )
                characterList.add(character)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return characterList
    }
}




