package reactivestreams.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JavaIOServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);
    private static AtomicInteger requestCount = new AtomicInteger(0);
    private static final String MESSAGE = "This is Server";

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            // 해당설정을 false로 변경시 nonBlocking 방식으로 동작 기본은 true (Blocking)
            serverSocketChannel.configureBlocking(false);

            while(true) {
                SocketChannel channel = serverSocketChannel.accept();
                if (channel == null) {
                    Thread.sleep(100);
                    log.info("await Client Connection");
                    continue;
                }

                /**
                 * handleRequest 메서드는 클라이언트로부터 데이터를 받고 응답하는 메서드로
                 * 클라이언트 소스를 보면 동시에 1000개에 요청을 비동기로 던지는데 이를
                 * main 스레드에서 받아 처리하게되면 단일스레드로 처리하기때문에 Blocking이
                 * 발생한다.
                 *
                 * 따라서 서버도 비동기적으로 클라이언트 accept가 발생할때마다
                 * 별도에 스레드에서 클라이언트에 데이터를 읽고 응답처리를 하면
                 * NonBlocking으로 동작
                */
                CompletableFuture.runAsync(() -> {
                    handleRequest(channel);
                }, executorService);
            }
        }
    }

    @SneakyThrows
    private static void handleRequest(SocketChannel channel) {
        /**
         * ByteBuffer.allocateDirect 메서드를 통해 리턴되는 버퍼는 DirectByteBuffer 버퍼로
         * 다른 ByteBuffer와는 다르게 커널에 Buffer에 바로 접근할수 있다.
         *
         * 다른 ByteBuffer는 커널버퍼에 데이터를 JVM 내부에 버퍼로 복사하고 JVM 내부에 버퍼를
         * 이용하기때문에 복사에대한 CPU 연산이 수행되어 추가적인 리소스 소모가있다.
         */
        ByteBuffer requestByteBuffer = ByteBuffer.allocateDirect(1024);
        channel.read(requestByteBuffer);
        requestByteBuffer.flip();

        // 응답까지에 지연요소
        Thread.sleep(100);

        log.info("request Body : {}", StandardCharsets.UTF_8.decode(requestByteBuffer));

        ByteBuffer responseByteBuffer = ByteBuffer.wrap(MESSAGE.getBytes());
        channel.write(responseByteBuffer);
        channel.close();

        int count = requestCount.incrementAndGet();
        log.info("requestCount : {}", count);
    }

}
