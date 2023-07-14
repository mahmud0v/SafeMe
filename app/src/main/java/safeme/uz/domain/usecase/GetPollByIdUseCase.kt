package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.response.PollDetailResponse
import safeme.uz.utils.RemoteApiResult

interface GetPollByIdUseCase {

    fun getPollById(id:Int): Flow<RemoteApiResult<ApiResponse<PollDetailResponse>>>

}