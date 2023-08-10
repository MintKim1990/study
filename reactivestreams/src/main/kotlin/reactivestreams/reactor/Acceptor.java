package reactivestreams.reactor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

@Slf4j
@RequiredArgsConstructor
public class Acceptor implements EventHandler {

    private final MessageEventHandler messageEventHandler;
    private final Selector selector;

    @SneakyThrows
    @Override
    public void handle(SelectionKey key) {
        // Selector에서 Accept 이벤트가 발생했을때 해당 정보를 담고있는 SelectionKey 정보를 받아 채널정보를 꺼내서
        // 클라이언트 소켓 생성 및 메세지를 처리하는 핸들러에게 전달
        SocketChannel clientSocket = ((ServerSocketChannel) key.channel()).accept();
        messageEventHandler.register(selector, clientSocket);
    }
}
