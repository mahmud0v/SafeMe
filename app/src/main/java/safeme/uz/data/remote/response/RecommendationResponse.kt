package safeme.uz.data.remote.response

data class RecommendationResponse(
    val success: Boolean,
    val message: String? = null,
    val body:RecommendationInfo,
    val code: Int
)
