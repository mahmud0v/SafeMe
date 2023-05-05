package safeme.uz.domain

import kotlinx.coroutines.flow.Flow
import safeme.uz.data.model.ResultData

interface LogOutUseCase {
    operator fun invoke(): Flow<ResultData<Boolean>>
}