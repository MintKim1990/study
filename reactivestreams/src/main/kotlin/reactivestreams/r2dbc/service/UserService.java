package reactivestreams.r2dbc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactivestreams.r2dbc.domain.UserRepository;
import reactivestreams.r2dbc.service.response.UserResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public Mono<UserResponse> findById(Long userId) {
        return userRepository.findById(userId).map(UserResponse::of);
    }

}
