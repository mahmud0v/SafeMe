package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.ResetPinCodeResponse

interface ResetPinCodeUseCase {
    fun resetPinCode(): Flow<ResultData<ResetPinCodeResponse>>
}