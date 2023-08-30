package reactivestreams.r2dbc.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactivestreams.r2dbc.service.UserService;
import reactivestreams.r2dbc.service.response.UserResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public Mono<UserResponse> findUser(@PathVariable Long userId) {
        return userService.findById(userId);
    }

    @GetMapping("/name")
    public Mono<String> findNameWithContext() {
        String principal = (String) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return Mono.just(principal);
    }

}
