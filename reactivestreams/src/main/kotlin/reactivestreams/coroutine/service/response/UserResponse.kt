package reactivestreams.coroutine.service.response

import reactivestreams.coroutine.domain.User

data class UserResponse(
    var id : Long,
    var name : String,
    var age: Int,
) {

    companion object {
        operator fun invoke(user: User) =
            with(user) {
                UserResponse(id!!, name, age)
            }
    }

}