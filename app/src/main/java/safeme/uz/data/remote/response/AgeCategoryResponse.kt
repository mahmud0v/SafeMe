package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName

data class AgeCategoryResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val body: ArrayList<T>? = null,
    val code: Int
)

data class AgeCategoryInfo(
    val id: Int,
    val title: String? = null,
    @SerializedName("year_from")
    val yearFrom: Int,
    @SerializedName("year_to")
    val yearTo: Int
)

