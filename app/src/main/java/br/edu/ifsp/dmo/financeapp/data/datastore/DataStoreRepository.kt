package br.edu.ifsp.dmo.financeapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import br.edu.ifsp.dmo.financeapp.util.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepository(context: Context) {
    private val dataStore: DataStore<Preferences> = context.dataStore

    object PreferencesFile {
        const val FILE_NAME = "user_preferences"
    }

    private object PreferencesKeys {
        val SAVE_LOGIN = booleanPreferencesKey("save_data")
        val STAY_LOGGED_IN = booleanPreferencesKey("stay_logged")
        val EMAIL = stringPreferencesKey("email")
        val PASSWORD = stringPreferencesKey("password")

        // Precisamos salvar o email do usuário, para o caso de ele se manter logado no sistema.
        // Precisamos do email para poder identificar o usuário.
        val STAY_LOGGED_EMAIL = stringPreferencesKey("stay_logged_email")
    }

    suspend fun savePreferences(
        email: String = "",
        password: String = "",
        saveLogin: Boolean,
        stayLoggedIn: Boolean
    ) {
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

    // Atualiza o valor do email do usuário logado.
    suspend fun saveEmailStayLogged(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.STAY_LOGGED_EMAIL] = email
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

    // Recebe o valor do email do usuário logado.
    val emailStayLogged: Flow<String> = dataStore.data.map { preferences ->
        val email = preferences[PreferencesKeys.STAY_LOGGED_EMAIL] ?: ""
        email
    }
}
