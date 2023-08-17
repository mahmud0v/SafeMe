package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.request.UserDataRequest
import safeme.uz.data.remote.response.UserDataResponse

interface SendUserDataUseCase {
    operator fun invoke (userDataRequest: UserDataRequest) : Flow<ResultData<UserDataResponse>>
}