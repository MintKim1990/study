package reactivestreams.coroutine.controller

import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactivestreams.coroutine.service.UserService
import reactivestreams.coroutine.service.response.UserResponse
import reactor.core.publisher.Mono

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    suspend fun findUser(@PathVariable userId: Long): UserResponse {
        return userService.findById(userId)
    }

    @GetMapping("/name")
    fun findNameWithContext(): Mono<String> {
        val principal = SecurityContextHolder
            .getContext()
            .authentication
            .principal as String

        return Mono.just(principal)
    }

}