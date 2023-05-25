package safeme.uz.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.InspectorMFYRequest
import safeme.uz.data.remote.response.InspectorInfo
import safeme.uz.data.repository.appeal.AppealRepository
import safeme.uz.domain.usecase.GetInspectorDataUseCase
import safeme.uz.utils.AnnouncementResult
import javax.inject.Inject

class GetInspectorDataUseCaseImpl @Inject constructor(
    private val appealRepository: AppealRepository
) : GetInspectorDataUseCase {

    override fun getInspectorDataByMFY(inspectorMFYRequest: InspectorMFYRequest): Flow<AnnouncementResult<ApiResponse<ArrayList<InspectorInfo>>>> {
        return flow {
            emit(AnnouncementResult.Loading())
            val response = appealRepository.getInspectorsByMFY(inspectorMFYRequest)
            val code = response.body()?.code
            if (code == 200) {
                emit(AnnouncementResult.Success(response.body()!!))
            } else {
                emit(AnnouncementResult.Error(response.body()?.message!!))
            }
        }
    }


}