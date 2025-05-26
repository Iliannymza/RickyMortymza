package com.example.rickymortymza.utils.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.rickymortymza.data.CharactersTable

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION  ){
    companion object {
        const val DATABASE_NAME = "rick_and_morty_db"
        const val DATABASE_VERSION = 1

        private const val SQL_CREATE_CHARACTERS =
            "CREATE TABLE ${CharactersTable.TABLE_NAME} (" +
                    "${CharactersTable.COLUMN_NAME_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "${CharactersTable.COLUMN_NAME_APIID} INTEGER," +
                    "${CharactersTable.COLUMN_NAME_NAME} TEXT," +
                    "${CharactersTable.COLUMN_NAME_STATUS} TEXT, " +
                    "${CharactersTable.COLUMN_NAME_SPECIES} TEXT, " +
                    "${CharactersTable.COLUMN_NAME_GENDER} TEXT, " +
                    "${CharactersTable.COLUMN_NAME_IMAGE} TEXT)"

        private const val SQL_DELETE_CHARACTERS = "DROP TABLE IF EXISTS ${CharactersTable.TABLE_NAME}"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CHARACTERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db :SQLiteDatabase){
        db.execSQL(SQL_DELETE_CHARACTERS)
    }


}

