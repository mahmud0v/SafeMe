package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName

data class PollDetailResponse(
    val id: Int? = null,
    val type: String? = null,
    val text: String? = null,
    val media: String? = null,
    val agecategory: Int? = null,
    @SerializedName("created_date")
    val createdDate: String? = null,
    val result: ArrayList<PollOptions>? = null
)

data class PollOptions(
    @SerializedName("id")
    val pollId: Int? = null,
    val text: String? = null
)