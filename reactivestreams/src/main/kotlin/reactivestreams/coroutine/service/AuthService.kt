package reactivestreams.coroutine.service

import org.springframework.stereotype.Service
import reactivestreams.coroutine.domain.AuthRepository
import reactor.core.publisher.Mono

@Service
class AuthService(
    private val authRepository: AuthRepository,
) {

    fun findnameByToken(token: String): Mono<String> {
        return authRepository.findnameByToken(token).last()
    }

}