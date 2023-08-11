package reactivestreams.proactor;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

@Slf4j
public class Proactor implements Runnable {

    private final AsynchronousServerSocketChannel serverSocketChannel;

    @SneakyThrows
    public Proactor(int port) {
        this.serverSocketChannel = AsynchronousServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
    }

    @Override
    public void run() {
        AcceptCompletionHandler acceptCompletionHandler = new AcceptCompletionHandler(serverSocketChannel);
        serverSocketChannel.accept(null, acceptCompletionHandler);
    }
}
