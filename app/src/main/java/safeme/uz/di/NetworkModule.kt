package safeme.uz.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import safeme.uz.BuildConfig
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.remote.AppApiService
import safeme.uz.data.remote.AuthApiService
import safeme.uz.data.remote.AuthAuthenticator
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @[Provides]
    fun tokenInterceptor(preference: AppSharedPreference): Interceptor {
        return Interceptor { chain ->
            val url = chain.request().url
            if (!(url.toString().contains("refresh"))) {
                val newRequest =
                    chain.request().newBuilder().addHeader("Authorization", preference.token)
                return@Interceptor chain.proceed(newRequest.build())
            } else {
                val request = chain.request().newBuilder()
                return@Interceptor chain.proceed(request.build())
            }
        }
    }

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        sharedPreference: AppSharedPreference
    ): AuthAuthenticator = AuthAuthenticator(sharedPreference)

    @[Provides Singleton]
    fun getOkHTTP(
        authAuthenticator: AuthAuthenticator,
        sharedPreference: AppSharedPreference,
        @ApplicationContext context:Context
    ): OkHttpClient {
      //  val loggingInterceptor = HttpLoggingInterceptor()
      //  loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient().newBuilder().readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS).callTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(tokenInterceptor(sharedPreference)).addInterceptor(ChuckerInterceptor.Builder(context).build())
            .authenticator(authAuthenticator).build()
    }

    @[Provides Singleton]
    fun provideRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @[Provides Singleton]
    fun provideAuthAPIService(
        retrofit: Retrofit
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @[Provides Singleton]
    fun provideMainAPIService(
        retrofit: Retrofit
    ): AppApiService = retrofit.create(AppApiService::class.java)
}