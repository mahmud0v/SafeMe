package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import android.provider.ContactsContract.DisplayPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.UserUpdateUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class UserUpdateUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository,
    private val application: Application
) : UserUpdateUseCase {

    override fun userUpdate(requestBody: MultipartBody): Flow<RemoteApiResult<ApiResponse<UserUpdateResponse>>> {

        return flow {
            emit(RemoteApiResult.Loading())
            val response = authRepository.userUpdate(requestBody)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.not_found)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


}