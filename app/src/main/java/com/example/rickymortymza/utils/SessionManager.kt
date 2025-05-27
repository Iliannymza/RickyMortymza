package com.example.rickymortymza.utils

import android.content.Context

class SessionManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("RickAndMorty_session", Context.MODE_PRIVATE)

    fun setLastDownload(timestamp: Long) {
        val editor = sharedPref.edit()
        editor.putLong("LAST_DOWNLOAD", timestamp)
        editor.apply()
    }

    fun getLastDownload(): Long {
        return sharedPref.getLong("LAST_DOWNLOAD", 0)
    }
}