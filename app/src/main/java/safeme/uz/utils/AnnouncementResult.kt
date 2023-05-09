package safeme.uz.utils

sealed class AnnouncementResult<T>(
    val data: T? = null,
    val message: String? = null
)
{
    class Success<T>(data: T) : AnnouncementResult<T>(data)
    class Error<T>(message: String) : AnnouncementResult<T>(message = message)
    class Loading<T> : AnnouncementResult<T>()
}