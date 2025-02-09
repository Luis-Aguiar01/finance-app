package br.edu.ifsp.dmo.financeapp.data.datastore

import android.content.Context
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit

import androidx.datastore.preferences.core.stringPreferencesKey
import br.edu.ifsp.dmo.financeapp.util.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository(context: Context)  {
    private val dataStore: DataStore<Preferences> = context.dataStore

    object PreferencesFile {
        const val FILE_NAME = "user_preferences"
    }

    private object PreferencesKeys {
        val SAVE_LOGIN = booleanPreferencesKey("save_data")
        val STAY_LOGGED_IN = booleanPreferencesKey("stay_logged")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")
    }

    suspend fun savePreferences(email: String = "", password: String = "", saveLogin: Boolean, stayLoggedIn: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.EMAIL] = email
            preferences[PreferencesKeys.PASSWORD] = password
            preferences[PreferencesKeys.SAVE_LOGIN] = saveLogin
            preferences[PreferencesKeys.STAY_LOGGED_IN] = stayLoggedIn
        }
    }

    suspend fun savePreferencesLogout() {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STAY_LOGGED_IN] = false
        }
    }

    val loginPreferences: Flow<Pair<Boolean, Boolean>> = dataStore.data.map { preferences ->
        val saveLogin = preferences[PreferencesKeys.SAVE_LOGIN] ?: false
        val stayLoggedIn = preferences[PreferencesKeys.STAY_LOGGED_IN] ?: false
        Pair(saveLogin, stayLoggedIn)
    }

    val dataPreferences: Flow<Pair<String, String>> = dataStore.data.map { preferences ->
        val email = preferences[PreferencesKeys.EMAIL] ?: ""
        val password = preferences[PreferencesKeys.PASSWORD] ?: ""
        Pair(email, password)
    }
}
