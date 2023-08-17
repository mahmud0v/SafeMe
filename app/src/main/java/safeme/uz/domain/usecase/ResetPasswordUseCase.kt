package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.ResetPasswordRequest
import safeme.uz.data.remote.response.PasswordUpdateBody

interface ResetPasswordUseCase {
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<ResultData<Boolean>>
}