package safeme.uz.data.remote.response

data class RegionInfo(
    val id: Int,
    val name: String? = null
) {

    override fun toString() = name ?: ""

}