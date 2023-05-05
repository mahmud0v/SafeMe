package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.RegisterRequest
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.VerifyRegisterResponse

interface VerifyRegisterUseCase {
    operator fun invoke(verifyRegisterRequest: VerifyRegisterRequest): Flow<ResultData<VerifyRegisterResponse>>
}