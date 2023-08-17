package safeme.uz.domain.usecase.impl

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import safeme.uz.R
import safeme.uz.data.local.sharedpreference.AppSharedPreference
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.RefreshTokenRequest
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.TokenUpdateUseCase
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class TokenUpdateUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
): TokenUpdateUseCase {

    override fun accessTokenUpdate(): Flow<RemoteApiResult<ApiResponse<LoginResponse>>> {
      return flow {
          val response = authRepository.accessTokenUpdate()
          when(response.code()){
              in 200..209 -> {
                  emit(RemoteApiResult.Success(response.body()))
              }

              401 -> {
                  emit(RemoteApiResult.Error(application.getString(R.string.unauthorized)))
              }

              else -> {
                  emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
              }

          }
      }
//          .catch {
//          it.printStackTrace()
//      }
    }
}