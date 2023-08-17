package safeme.uz.data.remote.api

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import safeme.uz.BuildConfig
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.RefreshTokenRequest
import safeme.uz.data.remote.response.LoginResponse
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val sharedPreference: AppSharedPreference,
    @ApplicationContext val context: Context,
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {

            val newToken = getNewToken(sharedPreference.locale, sharedPreference.refresh)
                ?: return@runBlocking null


            if (!newToken.success || newToken.body?.token == null) {
                sharedPreference.token = ""
            } else {

            }

            var retryCount = response.request.header("RetryCount")?.toInt() ?: 0
            retryCount += 1

            if (retryCount > 2) return@runBlocking null


            newToken.body?.let {
                sharedPreference.token = it.token.toString()
                sharedPreference.refresh = it.refresh.toString()
                response.request.newBuilder()
                    .header("RetryCount", "$retryCount")
                    .header("Authorization", "Bearer ${it.token}")
                    .build()
            }
        }
    }

    private suspend fun getNewToken(
        locale: String,
        refreshToken: String?
    ): ApiResponse<LoginResponse>? {
        return try {

            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            val service = retrofit.create(AuthApiService::class.java)
            val response = service.accessTokenUpdate(
                "${sharedPreference.locale}/user/token/refresh/",
                RefreshTokenRequest(sharedPreference.refresh)
            )
            when (response.code()) {
                in 200..201 -> response.body()
                else -> {
                    sharedPreference.token = ""
                    sharedPreference.refresh = ""
                    null
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }


}