package com.example.rickymortymza.utils

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.rickymortymza.data.Character
import com.example.rickymortymza.data.CharactersEpisodes
import com.example.rickymortymza.data.Episode

class DatabaseManager(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION  ){
    companion object {
        const val DATABASE_NAME = "rick_and_morty_db"
        const val DATABASE_VERSION = 3

        private const val SQL_CREATE_CHARACTERS =
            "CREATE TABLE ${Character.TABLE_NAME} (" +
                    "${Character.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                    "${Character.COLUMN_NAME_NAME} TEXT," +
                    "${Character.COLUMN_NAME_STATUS} TEXT, " +
                    "${Character.COLUMN_NAME_SPECIES} TEXT, " +
                    "${Character.COLUMN_NAME_GENDER} TEXT, " +
                    "${Character.COLUMN_NAME_IMAGE} TEXT, " +
                    "${Character.COLUMN_NAME_TYPE} TEXT, " +
                    "${Character.COLUMN_NAME_ORIGIN} TEXT, " +
                    "${Character.COLUMN_NAME_LOCATION} TEXT, " +
                    "${Character.COLUMN_NAME_URL} TEXT)"

        private const val SQL_CREATE_EPISODES =
            "CREATE TABLE ${Episode.TABLE_NAME} (" +
                    "${Episode.COLUMN_NAME_ID} INTEGER PRIMARY KEY," +
                    "${Episode.COLUMN_NAME_NAME} TEXT," +
                    "${Episode.COLUMN_NAME_AIR_DATE} TEXT," +
                    "${Episode.COLUMN_NAME_EPISODE_CODE} TEXT," +
                    "${Episode.COLUMN_NAME_URL} TEXT," +
                    "${Episode.COLUMN_NAME_CREATED} TEXT)"

        private const val SQL_CREATE_CHARACTER_EPISODE =
            "CREATE TABLE ${CharactersEpisodes.TABLE_NAME} (" +
                    "${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID} INTEGER," +
                    "${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID} INTEGER," +
                    "PRIMARY KEY (${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID}, " +
                    "${CharactersEpisodes.COLUMN_NAME_EPISODE_ID})," +
                    "FOREIGN KEY(${CharactersEpisodes.COLUMN_NAME_CHARACTER_ID}) " +
                    "REFERENCES " + "${Character.TABLE_NAME}(${Character.COLUMN_NAME_ID}) " +
                    "ON DELETE CASCADE," + "FOREIGN KEY(${CharactersEpisodes.COLUMN_NAME_EPISODE_ID}) " +
                    "REFERENCES " + "${Episode.TABLE_NAME}(${Episode.COLUMN_NAME_ID}) ON DELETE CASCADE)"



        private const val SQL_DELETE_CHARACTERS = "DROP TABLE IF EXISTS ${Character.TABLE_NAME}"

        private const val SQL_DELETE_EPISODES = "DROP TABLE IF EXISTS ${Episode.TABLE_NAME}"

        private const val SQL_DELETE_CHARACTER_EPISODE = "DROP TABLE IF EXISTS ${CharactersEpisodes.TABLE_NAME}"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_CHARACTERS)
        db.execSQL(SQL_CREATE_EPISODES)
        db.execSQL(SQL_CREATE_CHARACTER_EPISODE)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onDestroy(db)
        onCreate(db)
    }

    private fun onDestroy(db :SQLiteDatabase){
        db.execSQL(SQL_DELETE_CHARACTER_EPISODE)
        db.execSQL(SQL_DELETE_CHARACTERS)
        db.execSQL(SQL_DELETE_EPISODES)
    }
}

