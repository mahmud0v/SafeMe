package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName

data class GameRecommendationResponse(
    val id: Int? = null,
    val name: String? = null,
    val image: String? = null,
    @SerializedName("cat_name")
    val categoryName: String? = null,
    val agecategory: Int? = null,
    val category: Int? = null,
    val description: String? = null,
    val recommendation: String? = null,
    @SerializedName("developercompany")
    val developerCompany: String? = null,
    @SerializedName("created_date")
    val createdDate: String? = null
)