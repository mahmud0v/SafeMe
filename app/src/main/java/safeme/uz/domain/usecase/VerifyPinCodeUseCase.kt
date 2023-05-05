package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData

interface VerifyPinCodeUseCase {
    operator fun invoke(verification_code: String): Flow<ResultData<Unit>>
}