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
import safeme.uz.data.remote.api.AnnouncementApiService
import safeme.uz.data.remote.api.AppApiService
import safeme.uz.data.remote.api.AppealsApiService
import safeme.uz.data.remote.api.AuthApiService
import safeme.uz.data.remote.api.AuthAuthenticator
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
                val builder = chain.request().newBuilder()
                if (preference.token.isNotBlank()){
                    builder.addHeader("Authorization", preference.token)
                }
                return@Interceptor chain.proceed(builder.build())
            } else {
                val request = chain.request().newBuilder()
                return@Interceptor chain.proceed(request.build())
            }
        }
    }

    @Singleton
    @Provides
    fun provideAuthAuthenticator(
        sharedPreference: AppSharedPreference,
        @ApplicationContext context: Context
    ): AuthAuthenticator = AuthAuthenticator(sharedPreference,context)

    @[Provides Singleton]
    fun getOkHTTP(
        authAuthenticator: AuthAuthenticator,
        appSharedPreference: AppSharedPreference,
        @ApplicationContext context: Context
    ): OkHttpClient {

        return OkHttpClient().newBuilder()
            .writeTimeout(5,TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .connectTimeout(5, TimeUnit.MINUTES).callTimeout(5, TimeUnit.MINUTES)
//            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(tokenInterceptor(appSharedPreference))
            .authenticator(authAuthenticator).build()

    }

    @[Provides Singleton]
    fun provideGsonGsonConvertorFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @[Provides Singleton]
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @[Provides Singleton]
    fun provideAuthAPIService(
        retrofit: Retrofit
    ): AuthApiService = retrofit.create(AuthApiService::class.java)

    @[Provides Singleton]
    fun provideMainAPIService(
        retrofit: Retrofit
    ): AppApiService = retrofit.create(AppApiService::class.java)

    @[Provides Singleton]
    fun provideAnnouncementApiService(
        retrofit: Retrofit
    ): AnnouncementApiService = retrofit.create(AnnouncementApiService::class.java)

    @[Provides Singleton]
    fun provideAppealApiService(
        retrofit: Retrofit
    ): AppealsApiService = retrofit.create(AppealsApiService::class.java)


}