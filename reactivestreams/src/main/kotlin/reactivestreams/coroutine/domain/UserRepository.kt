package reactivestreams.coroutine.domain

import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

interface UserRepository : R2dbcRepository<User, Long>