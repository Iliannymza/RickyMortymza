package com.example.rickymortymza.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.rickymortymza.utils.database.DatabaseManager

/*class CharacterDao(private val context: Context) {
    // base de datos seguir a mansour
    private lateinit var db: SQLiteDatabase

    private fun open() {
        db = DatabaseManager(context).writableDatabase
    }

    private fun close() {
        db.close()
    }

    //INSERTAR
    fun insert(character: Character) {
        open()

        try {
            val value = ContentValues()
            value.put(Character.COLUMN_NAME, character.name)
        }
    }
}*/

