package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.PollAnswerRequest
import safeme.uz.data.remote.response.PollAnswerResponse
import safeme.uz.utils.RemoteApiResult

interface GivePollAnswerUseCase {

    fun givePollAnswer(pollAnswerRequest: PollAnswerRequest): Flow<RemoteApiResult<ApiResponse<PollAnswerResponse>>>



}