package reactivestreams.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JavaIOClient {


    private static final Integer PROCESSED_COUNT = 400;
    private static final String MESSAGE = "This is Client";
    private static ExecutorService executorService = Executors.newFixedThreadPool(PROCESSED_COUNT);

    @SneakyThrows
    public static void main(String[] args) {

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        long start = System.currentTimeMillis();

        for (int i = 0; i < PROCESSED_COUNT; i++) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                try (Socket socket = new Socket()) {
                    socket.connect(new InetSocketAddress("localhost", 8080));

                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(MESSAGE.getBytes());
                    outputStream.flush();

                    InputStream inputStream = socket.getInputStream();
                    byte[] responseBytes = new byte[1024];
                    inputStream.read(responseBytes);
                    log.info("response Body : {}", new String(responseBytes).trim());
                } catch (Exception e) {}
            }, executorService);

            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        executorService.shutdown();
        long end = System.currentTimeMillis();
        log.info("duration : {}", (end - start) / 1000);

    }
}
