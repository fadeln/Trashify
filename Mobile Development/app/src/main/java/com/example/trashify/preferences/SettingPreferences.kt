package com.example.trashify.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.trashify.helper.AuthData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth")
class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val TOKEN_KEY = stringPreferencesKey("token_key")
    private val USER_ID = stringPreferencesKey("user_ID")
    private val NAME = stringPreferencesKey("name")

    fun getCredential(): Flow<AuthData> {
        return dataStore.data.map{
                preferences ->
            AuthData(token = preferences[TOKEN_KEY], nama = preferences[NAME], userId = preferences[USER_ID])
        }
    }

    suspend fun saveCredential(data: AuthData){
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = data.token ?: ""
            preferences[USER_ID] = data.userId ?: ""
            preferences[NAME] = data.nama ?: ""
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: AuthPreferences? = null

        fun getInstance(dataStore: androidx.datastore.core.DataStore<androidx.datastore.preferences.core.Preferences>): AuthPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = AuthPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}