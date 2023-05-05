package safeme.uz.data.repository.app

import safeme.uz.data.model.Response

interface AppRepository {
    fun saveTokenToPreference()
    suspend fun logOut(): Response<Boolean>
}