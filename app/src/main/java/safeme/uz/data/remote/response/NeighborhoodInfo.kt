package safeme.uz.data.remote.response

data class NeighborhoodInfo(
    val id: Int,
    val name: String? = null,
    val region: Int,
    val district: Int
)