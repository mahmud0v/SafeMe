package safeme.uz.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class NewsData(
    val id:Int,
    val title:String? = null,
    val category:Int,
    @SerializedName("shorttext")
    val shortText:String? = null,
    val content:String? = null,
    val image:String? = null,
    val created_date:String? = null
) :Serializable
