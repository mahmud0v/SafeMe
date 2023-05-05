package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.AddingChildDataRequest
import safeme.uz.data.remote.response.AddingChildDataResponse

interface AddingChildDataUseCase {
    operator fun invoke(addingChildDataRequest: AddingChildDataRequest): Flow<ResultData<AddingChildDataResponse>>
}