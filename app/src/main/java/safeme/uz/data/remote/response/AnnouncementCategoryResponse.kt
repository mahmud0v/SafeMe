package safeme.uz.data.remote.response

data class AnnouncementCategoryResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val body: T? = null,
    val code: Int? = null
)

