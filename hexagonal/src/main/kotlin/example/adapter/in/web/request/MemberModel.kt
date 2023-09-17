package example.adapter.`in`.web.request

data class MemberCreateRequest(
    val name: String,
    val address: String,
    val email: String,
    val isCorp: Boolean,
)