package reactivestreams.coroutine.service

import org.springframework.stereotype.Service
import reactivestreams.coroutine.domain.UserRepository
import reactivestreams.coroutine.service.response.UserResponse

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    suspend fun findById(userId: Long): UserResponse {
        return userRepository.findById(userId).let { UserResponse(it!!) }
    }

}