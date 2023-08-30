package reactivestreams.r2dbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactivestreams.r2dbc.domain.AuthRepository;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final AuthRepository authRepository;

    public Mono<String> findnameByToken(String token) {
        return authRepository.findnameByToken(token).last();
    }

}
