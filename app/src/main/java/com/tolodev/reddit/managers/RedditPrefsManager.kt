package com.tolodev.reddit.managers

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class RedditPrefsManager(private val context: Context) : CoroutineScope {

    private val AUTHORIZATION_TOKEN = stringPreferencesKey("authorization_token")

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var job: Job = SupervisorJob()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

    suspend fun saveAuthorizationToken(token: String) {
        context.dataStore.edit { settings ->
            val currentCounterValue = settings[AUTHORIZATION_TOKEN].orEmpty()
            if (!token.equals(currentCounterValue, true)) {
                settings[AUTHORIZATION_TOKEN] = token
            }
        }
    }
}
