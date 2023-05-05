package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.Address

interface GetDistrictsByIdUseCase {
    operator fun invoke(regionId: Int): Flow<ResultData<List<Address>>>
}