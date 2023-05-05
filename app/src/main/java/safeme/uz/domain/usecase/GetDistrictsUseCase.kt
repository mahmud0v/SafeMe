package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.AddressResponse

interface GetDistrictsUseCase {
    operator fun invoke(): Flow<ResultData<AddressResponse>>
}