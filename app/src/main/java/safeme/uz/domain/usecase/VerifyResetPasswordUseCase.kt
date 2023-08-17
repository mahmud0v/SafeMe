package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.VerifyRegisterRequest
import safeme.uz.data.remote.response.VerifyResetPasswordResponse

interface VerifyResetPasswordUseCase {
    operator fun invoke(verification_code: String): Flow<ResultData<VerifyResetPasswordResponse?>>
}