package safeme.uz.data.remote.response

data class AddressResponse(
    val count: Int? = null,
    val next: Int? = null,
    val previous: Int? = null,
    val results: List<Address>? = emptyList(),
)

data class Address(
    val id: Int? = null,
    val name: String? = null,
    val region: Int? = null,
    val district: Int? = null,
    var type : Int? = null
) : java.io.Serializable