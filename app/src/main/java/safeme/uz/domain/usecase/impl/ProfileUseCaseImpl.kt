package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.data.repository.auth.AuthRepository
import safeme.uz.domain.usecase.ProfileUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

class ProfileUseCaseImpl @Inject constructor(
    private val authRepository: AuthRepository
) : ProfileUseCase {

    override fun getUserInfo(): Flow<AnnouncementResult<UserResponse>> {
        return flow {
            emit(AnnouncementResult.Loading())
            val response = authRepository.getUserInfo()
            val code = response.body()?.code
            if (code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.body()?.message!!))
            }
        }
    }
}