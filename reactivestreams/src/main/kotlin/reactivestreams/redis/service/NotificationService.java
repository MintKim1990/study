package reactivestreams.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.ReactiveStreamOperations;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.Map;

/**
 * 스프링에서 Reactive Redis 관련해서는 레포지토리를 지원하지 않고 가장 추상화된 버전이
 * ReactiveRedisTemplate 까지만 지원하여 이를 이용하여 메세지 스트림 구현
 */
@Slf4j
@Service
public class NotificationService {

    private static final String STREAM_NAME = "notification";
    private static final Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
    private static final StreamReceiver.StreamReceiverOptions<String, MapRecord<String, String, String>> STREAM_RECEIVER_OPTIONS =
            StreamReceiver.StreamReceiverOptions.builder()
                    .pollTimeout(Duration.ofMillis(100L))
                    .build();

    private ReactiveStreamOperations<String, Object, Object> streamOperations;
    private StreamReceiver<String, MapRecord<String, String, String>> streamReceiver;

    public NotificationService(ReactiveStringRedisTemplate redisTemplate) {
        this.streamOperations = redisTemplate.opsForStream();
        this.streamReceiver = StreamReceiver.create(redisTemplate.getConnectionFactory(), STREAM_RECEIVER_OPTIONS);

        StreamOffset<String> streamOffset = StreamOffset.create(STREAM_NAME, ReadOffset.latest());
        streamReceiver.receive(streamOffset)
                .subscribe(record -> {
                    String message = record.getValue().get("message");
                    sink.tryEmitNext(message);
                });
    }

    public Flux<String> getMessageFromSink() {
        return sink.asFlux();
    }

    public void tryEmitMessage(String message) {
        streamOperations.add(STREAM_NAME, Map.of("message", message)).subscribe();
    }

}
