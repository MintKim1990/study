package reactivestreams.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * MediaType.TEXT_EVENT_STREAM_VALUE 기반에 스트림 요청에서
 * 클라이언트에서 메세지 전송시 Redis Stream에 저장 후
 * Stream을 구독하고있는 StreamReceiver에서 메세지를 꺼내서
 * 고객에게 전달하는 간단한 Redis를 이용한 Notification 서비스 애플리케이션
 */
@SpringBootApplication
public class RedisApplication {
    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class);
    }
}
