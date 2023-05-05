package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.Address

interface GetMFYsByIdUseCase {
    operator fun invoke(districtId: Int): Flow<ResultData<List<Address>>>
}