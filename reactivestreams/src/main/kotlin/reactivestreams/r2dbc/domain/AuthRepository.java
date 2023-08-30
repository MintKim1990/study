package reactivestreams.r2dbc.domain;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthRepository extends R2dbcRepository<Auth, Long> {

    @Query("     select b.name " +
            "      from AUTH a " +
            "inner join USER b " +
            "        on a.user_id = b.id " +
            "     where a.token = :token")
    Flux<String> findnameByToken(String token);

}
