package safeme.uz.data.model

import java.io.Serializable


data class ManageScreen(
    val hostScreen: String? = null,
    val secondaryScreen: String? = null,
    var phoneNumber:String? = null
):Serializable