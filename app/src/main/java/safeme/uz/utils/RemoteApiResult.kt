package safeme.uz.utils

sealed class RemoteApiResult<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(data: T?) : RemoteApiResult<T>(data)
    class Error<T>(message: String) : RemoteApiResult<T>(message = message)
    class Loading<T> : RemoteApiResult<T>()
}