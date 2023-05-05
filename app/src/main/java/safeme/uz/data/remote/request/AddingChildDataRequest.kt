package safeme.uz.data.remote.request

data class AddingChildDataRequest(
    val name : String? = null,
    val gender : String? = null,
    val date_brithday : String? = null,
    val type_parrent : String? = null,
    val status : Boolean = false
)
