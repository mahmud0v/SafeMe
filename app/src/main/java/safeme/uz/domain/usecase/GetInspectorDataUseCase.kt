package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.utils.AnnouncementResult

interface GetInspectorDataUseCase {

    fun getInspectorDataByMFY(inspectorMFYRequest: InspectorMFYRequest): Flow<AnnouncementResult<ApiResponse<ArrayList<InspectorInfo>>>>



}