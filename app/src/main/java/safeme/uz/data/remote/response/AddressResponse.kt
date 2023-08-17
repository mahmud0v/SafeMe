package safeme.uz.data.remote.response

data class AddressResponse(
   val address:ArrayList<Address>?= null
)

//    val count: Int? = null,
//    val next: Int? = null,
//    val previous: Int? = null,
//    val results: List<Address>?= null,


data class Address(
    val id: Int? = null,
    val name: String? = null,
    val region: Int? = null,
    val district: Int? = null,
    var type : Int? = null
) : java.io.Serializable