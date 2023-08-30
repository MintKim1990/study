package reactivestreams.webflux;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.reactive.DispatcherHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.adapter.DefaultServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class TestMain {

    @SneakyThrows
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5)
                .log()
                .publishOn(Schedulers.parallel())
                .map(integer -> {
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {}
                    return integer;
                })
                .log()
                .subscribe();

        log.info("Main Exit");
        Thread.sleep(1000000);
    }
}
