package reactivestreams.coroutine.runner

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
        GlobalScope.launch {
            sampleDataInsert()
        }
    }

    suspend fun sampleDataInsert() {
        val user = User("name", 15, "password")
        userRepository.save(user)
            .let { Auth(it.id!!, "token") }
            .let { authRepository.save(it) }
    }

}