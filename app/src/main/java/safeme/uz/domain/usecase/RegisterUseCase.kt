package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.model.VerifyModel
import safeme.uz.data.remote.request.RegisterRequest

interface RegisterUseCase {
    operator fun invoke(registerRequest: VerifyModel): Flow<ResultData<VerifyModel>>
}