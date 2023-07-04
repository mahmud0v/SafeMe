package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.SosBody
import safeme.uz.data.remote.request.SosRequest
import safeme.uz.utils.RemoteApiResult

interface SosNotifiedUseCase {

    fun sosNotified(sosRequest: SosRequest):Flow<RemoteApiResult<ApiResponse<SosBody>>>


}