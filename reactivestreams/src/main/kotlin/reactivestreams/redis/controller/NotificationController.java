package reactivestreams.redis.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactivestreams.redis.service.NotificationService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/notifications")
@RestController
public class NotificationController {

    private static final AtomicInteger LAST_EVENT_ID = new AtomicInteger(1);
    private final NotificationService notificationService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> getNotifications() {
        return notificationService.getMessageFromSink()
                .map(message -> {
                    return ServerSentEvent
                            .builder(message)
                            .event("notification")
                            .id(String.valueOf(LAST_EVENT_ID.getAndIncrement()))
                            .comment("this is notification")
                            .build();
                });
    }

    @PostMapping
    public Mono<String> addNotification(@RequestBody Event event) {
        String message = event.getType() + ": " + event.getMessage();
        notificationService.tryEmitMessage(message);

        return Mono.just("ok");
    }

}
