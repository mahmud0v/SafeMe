package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData

interface ForgetPasswordUseCase {
    fun getVerificationCode(username: String? = null): Flow<ResultData<Boolean>>
}