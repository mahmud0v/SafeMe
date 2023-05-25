package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.remote.response.UserResponse
import safeme.uz.utils.AnnouncementResult

interface ProfileUseCase {
    fun getUserInfo():Flow<AnnouncementResult<UserResponse>>
}