package reactivestreams.r2dbc.runner;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactivestreams.r2dbc.domain.Auth;
import reactivestreams.r2dbc.domain.AuthRepository;
import reactivestreams.r2dbc.domain.User;
import reactivestreams.r2dbc.domain.UserRepository;
import reactor.core.publisher.Mono;

// 테스트데이터 입력하려는 목적
@RequiredArgsConstructor
@Component
public class UserSampleDataInsertRunner implements CommandLineRunner {

    private final AuthRepository authRepository;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        Mono.just(new User("name", 15, "password"))
                .flatMap(userRepository::save)
                .map(user -> new Auth(user.getId(), "token"))
                .flatMap(authRepository::save)
                .block();
    }
}
