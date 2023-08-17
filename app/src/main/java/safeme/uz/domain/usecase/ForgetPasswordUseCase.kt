package safeme.uz.domain.usecase

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ApiResponse
import safeme.uz.data.model.ResultData
import safeme.uz.data.remote.response.RegisterResponse
import safeme.uz.utils.RemoteApiResult

interface ForgetPasswordUseCase {
    fun getVerificationCode(username: String? = null): Flow<ResultData<Boolean>>

    fun sendSms(phone:String):Flow<RemoteApiResult<ApiResponse<RegisterResponse>>>
}