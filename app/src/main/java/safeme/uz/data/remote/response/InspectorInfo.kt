package safeme.uz.data.remote.response

import com.google.gson.annotations.SerializedName

data class InspectorInfo(
  val id:Int,
  @SerializedName("first_name")
  val firstName:String? = null,
  @SerializedName("last_name")
  val lastName:String? = null,
  val patranomic:String? = null,
  var lavozimi:String? = null,
  val unvoni:String? = null,
  val phone:String? = null,
  val image:String? = null,
  val region:String? = null,
  val district:String? = null,
  var mahalla:String? = null


)