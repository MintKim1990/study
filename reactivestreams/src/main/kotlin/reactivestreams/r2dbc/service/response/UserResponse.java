package reactivestreams.r2dbc.service.response;

import reactivestreams.r2dbc.domain.User;

public record UserResponse(
        Long id,
        String name,
        Integer age
) {
    public static UserResponse of(User user) { return new UserResponse(user.getId(), user.getName(), user.getAge()); }
}
