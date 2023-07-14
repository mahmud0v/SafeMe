package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.utils.RemoteApiResult

interface PollQuestionUseCase {

    fun getPollsByAgeCategory(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<PollResponseInfo>>>>



}