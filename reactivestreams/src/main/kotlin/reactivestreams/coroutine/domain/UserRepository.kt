package reactivestreams.coroutine.domain

import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface UserRepository : CoroutineSortingRepository<User, Long>