package reactivestreams.reactor;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * TCPEventLoop 클래스는 Selector를 이용하여 TCP 통신관련 이벤트를 처리하는 이벤트 처리자이며
 * 이런 다양한 이벤트들을 처리하는 서버를 여러대 띄워져있는 형상이 아주 허접한 Netty와 비슷한 모양이다.
 * 자세한 코드는 EventLoop 참고
 */
@Slf4j
public class ReactorMain {
    public static void main(String[] args) {
        List<EventLoop> TCPEventLoops = List.of(new TCPEventLoop(8080));
        TCPEventLoops.forEach(EventLoop::run);
    }
}
