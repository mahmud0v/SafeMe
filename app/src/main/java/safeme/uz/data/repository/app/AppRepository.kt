package safeme.uz.data.repository.app

import safeme.uz.data.model.ApiResponse

interface AppRepository {
    fun saveTokenToPreference()
    suspend fun logOut(): ApiResponse<Boolean>

}