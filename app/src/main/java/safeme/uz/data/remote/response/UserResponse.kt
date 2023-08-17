package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import java.io.Serializable


data class UserResponse(
    val success: Boolean,
    val message: String? = null,
    val body: UserInfo? = null,
    val code: Int
)


data class UserInfo(
    val id: Int,
    val phone: String? = null,
    @SerializedName("first_name")
    val firstName: String? = null,
    @SerializedName("last_name")
    val lastName: String? = null,
    @SerializedName("birth_day")
    val birthDay: String? = null,
    val gender: String? = null,
    val region: String? = null,
    val district: String? = null,
    val mahalla: String? = null,
    val adress: String? = null,
    val photo:String? = null

):Serializable