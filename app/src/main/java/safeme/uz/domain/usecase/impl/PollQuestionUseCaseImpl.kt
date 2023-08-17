package safeme.uz.domain.usecase.impl

import android.app.Application
import android.content.res.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import safeme.uz.R
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.remote.request.AgeCategoryRequest
import safeme.uz.data.remote.response.PollResponseInfo
import safeme.uz.data.repository.announcement.AnnouncementRepository
import safeme.uz.domain.usecase.PollQuestionUseCase
import safeme.uz.utils.Keys
import safeme.uz.utils.RemoteApiResult
import javax.inject.Inject

class PollQuestionUseCaseImpl @Inject constructor(
    private val announcementRepository: AnnouncementRepository,
    private val application: Application
) : PollQuestionUseCase {


    override fun getPollsByAgeCategory(ageCategoryRequest: AgeCategoryRequest): Flow<RemoteApiResult<ApiResponse<ArrayList<PollResponseInfo>>>> {
        return flow {
            emit(RemoteApiResult.Loading())
            val response = announcementRepository.getPollByAgeCategory(ageCategoryRequest)
            when(response.code()){
                in 200..209 -> emit(RemoteApiResult.Success(response.body()!!))
                404 -> emit(RemoteApiResult.Error(application.getString(R.string.not_found)))
                in 500..509 -> emit(RemoteApiResult.Error(application.getString(R.string.internal_server_error)))
                else -> emit(RemoteApiResult.Error(application.getString(R.string.some_error_occurred)))
            }
        }
    }


}