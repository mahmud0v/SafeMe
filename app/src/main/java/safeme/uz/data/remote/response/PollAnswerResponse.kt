package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName

data class PollAnswerResponse(
    val id: Int? = null,
    val type: String? = null,
    val text: String? = null,
    val media: String? = null,
    val agecategory: Int? = null,
    @SerializedName("created_date")
    val createdDate: String? = null,
    val user: Int? = null,
    val answer: ArrayList<PollAnswerCount>? = null
)

data class PollAnswerCount(
    val answer: Int? = null,
    val count: Int? = null
)