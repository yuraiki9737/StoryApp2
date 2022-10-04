package com.navigation.latihan.storyappsubmission1intermediateandroid.data.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.navigation.latihan.storyappsubmission1intermediateandroid.data.model.LoginUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceAkunStory  (private val dataStoreStoryApp: DataStore<Preferences>) {

    fun getAkunStory(): Flow<LoginUser>{
        return dataStoreStoryApp.data.map { preferences ->
            LoginUser(
                preferences[ACCOUT_NAMA_KEY]?:"",
                preferences[ACCOUNT_EMAIL_KEY]?:"",
                preferences[ACCOUNT_USERID_KEY] ?: "",
                preferences[ACCOUNT_TOKEN_KEY]?:"",
                preferences[ACCOUNT_STATE_KEY]?: false
            )
        }
    }

    suspend fun saveStoryApp(loginStory:LoginUser){
        dataStoreStoryApp.edit { preferences ->
            preferences[ACCOUT_NAMA_KEY] = loginStory.name
            preferences[ACCOUNT_EMAIL_KEY]= loginStory.email
            preferences[ACCOUNT_USERID_KEY] = loginStory.userId
            preferences[ACCOUNT_TOKEN_KEY]= loginStory.token
            preferences[ACCOUNT_STATE_KEY]= loginStory.isLogin

        }
    }


    suspend fun logoutStoryApp(){
        dataStoreStoryApp.edit { preferences ->
            preferences[ACCOUNT_STATE_KEY] = false
            preferences[ACCOUT_NAMA_KEY] = ""
            preferences[ACCOUNT_EMAIL_KEY]= ""
            preferences[ACCOUNT_USERID_KEY] = ""
            preferences[ACCOUNT_TOKEN_KEY]= ""

        }
    }

    companion object{
        @Volatile
        private var INSTANCESTORYAPP : PreferenceAkunStory? = null

        private val ACCOUNT_USERID_KEY = stringPreferencesKey("userId")
        private val ACCOUNT_EMAIL_KEY = stringPreferencesKey("email")
        private val ACCOUT_NAMA_KEY = stringPreferencesKey("name")
        private val ACCOUNT_TOKEN_KEY = stringPreferencesKey("token")
        private val ACCOUNT_STATE_KEY = booleanPreferencesKey("state")

        fun getInstanceStoryApp(dataStoreStoryApp: DataStore<Preferences>): PreferenceAkunStory {
            return INSTANCESTORYAPP ?: synchronized(this){
                val instanceStoryApp = PreferenceAkunStory(dataStoreStoryApp)
                INSTANCESTORYAPP = instanceStoryApp
                instanceStoryApp
            }
        }
    }

}