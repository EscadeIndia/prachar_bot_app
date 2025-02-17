package com.prachaarbot.data.local.sharedPreference

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.GsonBuilder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppSharedPreferenceProvider @Inject constructor(@ApplicationContext context: Context) {
    private val keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC
    private val mainKeyAlias = MasterKeys.getOrCreate(keyGenParameterSpec)
    private val sharedPrefsFile: String = "PracharAppSharedPref"
    private val sharedPreferences: SharedPreferences = EncryptedSharedPreferences.create(
        sharedPrefsFile,
        mainKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun getString(key: String?, default: String?): String? =
        sharedPreferences.getString(key, default)

    fun getStringSet(key: String?, default: Set<String?>?): Set<String?>? =
        sharedPreferences.getStringSet(key, default)

    fun getInt(key: String?, default: Int): Int = sharedPreferences.getInt(key, default)
    fun getLong(key: String?, default: Long): Long =
        sharedPreferences.getLong(key, default)

    fun getFloat(key: String?, default: Float): Float =
        sharedPreferences.getFloat(key, default)

    fun getBoolean(key: String?, default: Boolean): Boolean =
        sharedPreferences.getBoolean(key, default)

    fun contains(key: String?): Boolean = sharedPreferences.contains(key)

    fun putString(key: String?, value: String?) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            commit()
        }
    }

    fun putStringSet(key: String?, value: Set<String?>?) {
        with(sharedPreferences.edit()) {
            putStringSet(key, value)
            commit()
        }
    }

    fun putInt(key: String?, value: Int) {
        with(sharedPreferences.edit()) {
            putInt(key, value)
            commit()
        }
    }

    fun putLong(key: String?, value: Long) {
        with(sharedPreferences.edit()) {
            putLong(key, value)
            commit()
        }
    }

    fun putFloat(key: String?, value: Float) {
        with(sharedPreferences.edit()) {
            putFloat(key, value)
            commit()
        }
    }

    fun putBoolean(key: String?, value: Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    fun <T> put(data: T, key: String) {
        val jsonString = GsonBuilder().create().toJson(data)
        with(sharedPreferences.edit()) {
            putString(key, jsonString).apply()
        }
    }

    fun clearData() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}