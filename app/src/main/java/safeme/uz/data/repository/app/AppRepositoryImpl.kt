package safeme.uz.data.repository.app

import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.Response
import safeme.uz.data.remote.api.AppApiService
import javax.inject.Inject


class AppRepositoryImpl @Inject constructor(
    private val api: AppApiService,
    private val sharedPreference: AppSharedPreference
) : AppRepository {

    override fun saveTokenToPreference() {
        sharedPreference.token = ""
        sharedPreference.refresh = ""
    }

    override suspend fun logOut(): Response<Boolean> {
        return api.logout("${sharedPreference.locale}/user/logout/")
    }


}