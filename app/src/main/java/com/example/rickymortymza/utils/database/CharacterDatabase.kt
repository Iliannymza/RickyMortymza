package com.example.rickymortymza.utils.database

/*import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_GENDER
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_ID
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_IMAGE
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_NAME
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_SPECIES
import com.example.rickymortymza.utils.database.DatabaseManager.COLUMN_STATUS
import com.example.rickymortymza.utils.database.DatabaseManager.DATABASE_NAME
import com.example.rickymortymza.utils.database.DatabaseManager.DATABASE_VERSION
import com.example.rickymortymza.utils.database.DatabaseManager.TABLE_CHARACTERS

class CharacterDatabase(context: Context) : SQLiteDatabase(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CHARACTERS_TABLE = """
            CREATE TABLE $TABLE_CHARACTERS (
                $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_STATUS TEXT,
                $COLUMN_SPECIES TEXT,
                $COLUMN_GENDER TEXT,
                $COLUMN_IMAGE TEXT
            )
            """.trimIndent() //quitar espacios innecesarios
            db?.execSQL(CREATE_CHARACTERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CHARACTERS")
        onCreate(db)
    }
}*/