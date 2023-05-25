package safeme.uz.data.model

data class ApiResponse<T>(
    var success: Boolean = false,
    var message: String? = null,
    var body: T? = null,
    var code: Int? = null
)
