package safeme.uz.data.model

import java.io.Serializable

data class VerifyModel(
    val phoneNumber: String? = null,
    val title: String? = null,
    val type: Int? = null,
    val password: String? = null,
    val manageScreen:ManageScreen? = null
) : Serializable
