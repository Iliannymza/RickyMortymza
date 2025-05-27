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

    fun insert(character: CharactersTable) {
        open()

        try {
            val values = ContentValues().apply {
                put(CharactersTable.COLUMN_NAME_APIID, character.apiid)
                put(CharactersTable.COLUMN_NAME_NAME, character.name)
                put(CharactersTable.COLUMN_NAME_STATUS, character.status)
                put(CharactersTable.COLUMN_NAME_SPECIES, character.species)
                put(CharactersTable.COLUMN_NAME_GENDER, character.gender)
                put(CharactersTable.COLUMN_NAME_IMAGE, character.image)
            }

            val newRowId = db.insert(CharactersTable.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted a character with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Actualizar un personaje
    fun update(character: CharactersTable) {
        open()

        try {
            val values = ContentValues().apply {
                put(CharactersTable.COLUMN_NAME_APIID, character.apiid)
                put(CharactersTable.COLUMN_NAME_NAME, character.name)
                put(CharactersTable.COLUMN_NAME_STATUS, character.status)
                put(CharactersTable.COLUMN_NAME_SPECIES, character.species)
                put(CharactersTable.COLUMN_NAME_GENDER, character.gender)
                put(CharactersTable.COLUMN_NAME_IMAGE, character.image)
            }

            val selection = "${CharactersTable.COLUMN_NAME_ID} = ${character.id}"

            val count = db.update(CharactersTable.TABLE_NAME, values, selection, null)

            Log.i("DATABASE", "Updated character with id: ${character.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Borrar un personaje
    fun delete(character: CharactersTable) {
        open()

        try {
            val selection = "${CharactersTable.COLUMN_NAME_ID} = ${character.id}"

            val deletedRows = db.delete(CharactersTable.TABLE_NAME, selection, null)

            Log.i("DATABASE", "Deleted character with id: ${character.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }
    }

    // Obtener un registro por ID de la base de datos local
    fun findById(id: Long): CharactersTable? {
        open()

        var character: CharactersTable? = null
        try {
            val projection = arrayOf(
                CharactersTable.COLUMN_NAME_ID,
                CharactersTable.COLUMN_NAME_APIID,
                CharactersTable.COLUMN_NAME_NAME,
                CharactersTable.COLUMN_NAME_STATUS,
                CharactersTable.COLUMN_NAME_SPECIES,
                CharactersTable.COLUMN_NAME_GENDER,
                CharactersTable.COLUMN_NAME_IMAGE
            )

            val selection = "${CharactersTable.COLUMN_NAME_ID} = $id"

            val cursor = db.query(
                CharactersTable.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_ID))
                val apiId = cursor.getInt(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_APIID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_NAME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_STATUS))
                val species = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_SPECIES))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_GENDER))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_IMAGE))

                character = CharactersTable(localId, apiId, name, status, species, gender, image)
            }

            cursor.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            close()
        }

        return character
    }

    // Obtener un registro por API ID
    fun findByApiId(apiId: Int): CharactersTable? {
        open()

        var character: CharactersTable? = null
        try {
            val projection = arrayOf(
                CharactersTable.COLUMN_NAME_ID,
                CharactersTable.COLUMN_NAME_APIID,
                CharactersTable.COLUMN_NAME_NAME,
                CharactersTable.COLUMN_NAME_STATUS,
                CharactersTable.COLUMN_NAME_SPECIES,
                CharactersTable.COLUMN_NAME_GENDER,
                CharactersTable.COLUMN_NAME_IMAGE
            )

            val selection = "${CharactersTable.COLUMN_NAME_APIID} = $apiId"

            val cursor = db.query(
                CharactersTable.TABLE_NAME,
                projection,
                selection,
                null,
                null,
                null,
                null
            )

            if (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_ID))
                val foundApiId = cursor.getInt(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_APIID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_NAME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_STATUS))
                val species = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_SPECIES))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_GENDER))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_IMAGE))

                character = CharactersTable(localId, foundApiId, name, status, species, gender, image)
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
    fun findAll(): List<CharactersTable> {
        open()

        val characterList: MutableList<CharactersTable> = mutableListOf()
        try {
            val projection = arrayOf(
                CharactersTable.COLUMN_NAME_ID,
                CharactersTable.COLUMN_NAME_APIID,
                CharactersTable.COLUMN_NAME_NAME,
                CharactersTable.COLUMN_NAME_STATUS,
                CharactersTable.COLUMN_NAME_SPECIES,
                CharactersTable.COLUMN_NAME_GENDER,
                CharactersTable.COLUMN_NAME_IMAGE
            )

            val cursor = db.query(
                CharactersTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
            )

            while (cursor.moveToNext()) {
                val localId = cursor.getLong(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_ID))
                val apiId = cursor.getInt(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_APIID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_NAME))
                val status = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_STATUS))
                val species = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_SPECIES))
                val gender = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_GENDER))
                val image = cursor.getString(cursor.getColumnIndexOrThrow(CharactersTable.COLUMN_NAME_IMAGE))

                val character = CharactersTable(localId, apiId, name, status, species, gender, image)
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



