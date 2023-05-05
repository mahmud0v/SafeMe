package safeme.uz.data.remote

import android.util.Log
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import safeme.uz.BuildConfig
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.remote.request.RefreshTokenRequest
import safeme.uz.data.remote.response.LoginResponse
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val sharedPreference: AppSharedPreference,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            val newToken = getNewToken(sharedPreference.locale, sharedPreference.refresh)

            if (!newToken.success || newToken.body?.token == null) {
                sharedPreference.token = ""
            }

            newToken.body?.let {
                sharedPreference.token = it.token.toString()
                sharedPreference.refresh = it.refresh.toString()
                response.request.newBuilder()
                    .header("Authorization", "Bearer ${it.token}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(
        locale: String,
        refreshToken: String?
    ): safeme.uz.data.model.Response<LoginResponse> {

        Log.e("TAG", "getNewToken: ffffffffffffffffffffffffffffffffffffffffffff", )

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

        val service = retrofit.create(AuthApiService::class.java)

        return service.refreshToken(
            "$locale/user/token/refresh/",
            RefreshTokenRequest(refreshToken)
        )
    }

}