package safeme.uz.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import safeme.uz.data.repository.app.AppRepository
import safeme.uz.data.repository.app.AppRepositoryImpl
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.data.repository.auth.AuthRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun getAppRepository(impl: AppRepositoryImpl): AppRepository

    @[Binds Singleton]
    fun getAuthRepository(impl: AuthRepositoryImpl): AuthRepository
}