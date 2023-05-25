package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.api.AuthApiService
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.UserUpdateUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

class UserUpdateUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : UserUpdateUseCase {

    override fun userUpdate(userUpdateRequest: UserUpdateRequest): Flow<AnnouncementResult<ApiResponse<UserUpdateResponse>>> {

        return flow {
            emit(AnnouncementResult.Loading())
            val response = authRepository.userUpdate(userUpdateRequest)
            val code = response.body()?.code
            if (code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.body()!!.message!!))
            }
        }
    }


}