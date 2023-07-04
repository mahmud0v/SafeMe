package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.utils.RemoteApiResult

interface GetInspectorDataUseCase {

    fun getInspectorDataByMFY(inspectorMFYRequest: InspectorMFYRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<InspectorInfo>>>>



}