package reactivestreams.coroutine.domain

import org.springframework.data.r2dbc.repository.Query
import org.springframework.data.repository.kotlin.CoroutineSortingRepository
import reactor.core.publisher.Flux

interface AuthRepository : CoroutineSortingRepository<Auth, Long> {

    @Query("     select b.name " +
            "      from AUTH a " +
            "inner join USER b " +
            "        on a.user_id = b.id " +
            "     where a.token = :token")
    fun findnameByToken(token: String): Flux<String>

}