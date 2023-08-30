package reactivestreams.r2dbc.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactivestreams.r2dbc.service.AuthService;
import reactor.core.publisher.Mono;

import javax.xml.namespace.QName;

@RequiredArgsConstructor
@Component
public class ContextFilter implements WebFilter {

    private final AuthService authService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (StringUtils.hasText(token)) {
            return authService.findnameByToken(token)
                    .flatMap(name -> {
                        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(name, ""));
                        return chain.filter(exchange);
                    });
        }

        return chain.filter(exchange);
    }
}
