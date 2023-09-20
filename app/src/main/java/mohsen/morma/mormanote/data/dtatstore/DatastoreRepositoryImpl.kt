package mohsen.morma.mormanote.data.dtatstore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "MormaDatastore")

class DatastoreRepositoryImpl @Inject constructor(private val context: Context) :
    DatastoreRepository {

    override suspend fun putString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        context.datastore.edit { it[preferencesKey] = value }
    }

    override suspend fun getString(key: String): String? = try {

            val preferencesKey = stringPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    override suspend fun putInt(key: String, value: Int) {
        val preferencesKey = intPreferencesKey(key)
        context.datastore.edit { it[preferencesKey] = value }
    }

    override suspend fun getInt(key: String): Int? {
        return try {

            val preferencesKey = intPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]

        }
        catch (e:Exception){
            e.printStackTrace()
            null
        }
    }

    override suspend fun putBool(key: String, value: Boolean) {
        val preferencesKey = booleanPreferencesKey(key)
        context.datastore.edit { it[preferencesKey] = value }
    }

    override suspend fun getBool(key: String): Boolean? =
        try {
            val preferencesKey = booleanPreferencesKey(key)
            val preferences = context.datastore.data.first()
            preferences[preferencesKey]
        }  catch (e : Exception){
            e.printStackTrace()
            null
        }
}