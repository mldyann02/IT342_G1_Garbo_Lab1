package com.cit.authapplication.network

import android.content.Context

object TokenManager {
    private const val PREFS = "auth_prefs"
    private const val KEY_TOKEN = "auth_token"

    fun saveToken(context: Context, token: String?) {
        token?.let {
            context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putString(KEY_TOKEN, it).apply()
        }
    }


    fun getToken(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getString(KEY_TOKEN, null)
    }

    fun clear(context: Context) {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}
