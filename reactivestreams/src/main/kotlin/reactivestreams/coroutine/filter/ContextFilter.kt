package reactivestreams.coroutine.filter

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactivestreams.coroutine.service.AuthService
import reactor.core.publisher.Mono

@Component
class ContextFilter(
    private val authService: AuthService,
) : WebFilter {

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val token = exchange.request.headers.getFirst("Authorization")

        return if (StringUtils.hasText(token)) {
            authService.findnameByToken(token!!)
                .flatMap {
                    SecurityContextHolder.getContext().authentication =
                        UsernamePasswordAuthenticationToken(it, "")
                    chain.filter(exchange)
                }
        } else chain.filter(exchange)

    }

}