package safeme.uz.data.remote.response

data class RecommendationInfoResponse(
    val success: Boolean,
    val message: String? = null,
    val body: ArrayList<RecommendationInfo>,
    val code: Int
)


data class RecommendationInfo(
    val id: Int? = null,
    val title: String? = null,
    val category: Int? = null,
    val agecategory: Int? = null,
    val image: String? = null,
    val shorttext: String? = null,
    val text: String? = null,
    val created_date: String? = null
)