package reactivestreams.coroutine.runner

import kotlinx.coroutines.*
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import reactivestreams.coroutine.domain.Auth
import reactivestreams.coroutine.domain.AuthRepository
import reactivestreams.coroutine.domain.User
import reactivestreams.coroutine.domain.UserRepository

@Component
class UserSampleDataInsertRunner(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        CoroutineScope(Dispatchers.IO).async {
            sampleDataInsert()
        }
    }

    suspend fun sampleDataInsert() {
        userRepository.save(User("name", 15, "password"))
            .awaitSingle()
            .let {
                Auth(it.id!!, "token")
            }
            .let { authRepository.save(it).awaitSingle() }
    }

}