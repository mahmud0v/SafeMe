package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.utils.AnnouncementResult

interface UserUpdateUseCase {

    fun userUpdate(userUpdateRequest: UserUpdateRequest): Flow<AnnouncementResult<ApiResponse<UserUpdateResponse>>>
}