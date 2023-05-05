package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.LoginRequest
import safeme.uz.data.remote.response.LoginResponse

interface LoginUseCase {
    operator fun invoke(loginRequest: LoginRequest): Flow<ResultData<LoginResponse>>
}