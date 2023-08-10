package reactivestreams.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class JavaSocketChannelClient {

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);
    private static AtomicInteger requestCount = new AtomicInteger(0);
    private static final String MESSAGE = "This is Client";

    @SneakyThrows
    public static void main(String[] args) {

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < 5000; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try (SocketChannel socketChannel = SocketChannel.open()) {
                    socketChannel.connect(new InetSocketAddress("localhost", 8080));

                    socketChannel.write(ByteBuffer.wrap(MESSAGE.getBytes()));

                    ByteBuffer responseByteBuffer = ByteBuffer.allocateDirect(1024);
                    while (socketChannel.read(responseByteBuffer) > 0) {
                        responseByteBuffer.flip();

                        String requestBody = StandardCharsets.UTF_8.decode(responseByteBuffer).toString();
                        log.info("response Body : {}", requestBody);

                        responseByteBuffer.clear();
                    }

                    int requestCount = JavaSocketChannelClient.requestCount.incrementAndGet();
                    log.info("requestCount = {}", requestCount);

                } catch (IOException error) {}
            }, executorService);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
        long end = System.currentTimeMillis();
        log.info("duration : {}", (end - start) / 1000);

    }
}
