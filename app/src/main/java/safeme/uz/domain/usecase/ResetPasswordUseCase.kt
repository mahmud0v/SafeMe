package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.ResetPasswordRequest

interface ResetPasswordUseCase {
    fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<ResultData<Boolean>>
}