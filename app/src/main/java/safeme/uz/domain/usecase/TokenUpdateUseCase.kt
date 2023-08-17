package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.RefreshTokenRequest
import safeme.uz.data.remote.response.LoginResponse
import safeme.uz.utils.RemoteApiResult

interface TokenUpdateUseCase {

    fun accessTokenUpdate():Flow<RemoteApiResult<ApiResponse<LoginResponse>>>
}