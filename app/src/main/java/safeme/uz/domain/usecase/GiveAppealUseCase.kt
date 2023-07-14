package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AppealRequest
import safeme.uz.data.remote.response.AppealResponse
import safeme.uz.utils.RemoteApiResult

interface GiveAppealUseCase {

    fun giveAppeal(appealRequest: AppealRequest) : Flow<RemoteApiResult<ApiResponse<AppealResponse>>>
}