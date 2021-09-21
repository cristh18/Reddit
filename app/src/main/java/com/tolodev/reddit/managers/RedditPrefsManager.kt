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
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

class RedditPrefsManager(private val context: Context) : CoroutineScope {

    private val AUTHORIZATION_TOKEN = stringPreferencesKey("authorization_token")

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var job: Job = SupervisorJob()

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "authentication")

    val authorizationTokenFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[AUTHORIZATION_TOKEN].orEmpty()
        }

    fun authorizationToken() = runBlocking {
        val result = async { authorizationTokenFlow.first() }
        return@runBlocking result.await()
    }

    suspend fun saveAuthorizationToken(token: String) {
        context.dataStore.edit { settings ->
            val currentCounterValue = settings[AUTHORIZATION_TOKEN].orEmpty()
            if (!token.equals(currentCounterValue, true)) {
                settings[AUTHORIZATION_TOKEN] = token
            }
        }
    }
}
