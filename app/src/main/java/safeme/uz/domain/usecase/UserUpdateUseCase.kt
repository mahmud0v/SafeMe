package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.UserUpdateRequest
import safeme.uz.data.remote.response.UserUpdateResponse
import safeme.uz.utils.RemoteApiResult

interface UserUpdateUseCase {

    fun userUpdate(requestBody: MultipartBody): Flow<RemoteApiResult<ApiResponse<UserUpdateResponse>>>
}