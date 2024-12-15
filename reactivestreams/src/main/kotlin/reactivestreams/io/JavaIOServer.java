package reactivestreams.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class JavaIOServer {

    private static final Integer PROCESSED_COUNT = 200;
    private static final String MESSAGE = "This is Server";
    private static final ExecutorService executorService = Executors.newFixedThreadPool(PROCESSED_COUNT);

    @SneakyThrows
    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while(true) {
                Socket clientSocket = serverSocket.accept(); // 블락킹으로 동작

                // 동시에 200개에 요청이 처리가능하며
                // 사용자에 요청을 받아 1초가 걸리는 어떠한 작업을 수행 후 사용자에게 응답 리턴
                executorService.submit(() -> {
                    try {
                        readRequest(clientSocket);
                        Thread.sleep(1000);
                        writeResponse(clientSocket);
                    } catch (Exception e) {}
                });
            }
        } finally {
            executorService.shutdown();
        }
    }

    private static void readRequest(Socket clientSocket) throws IOException {
        byte[] requestBytes = new byte[1024];
        InputStream inputStream = clientSocket.getInputStream();
        inputStream.read(requestBytes);
        log.info("request Body : {}", new String(requestBytes).trim());
    }

    private static void writeResponse(Socket clientSocket) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(MESSAGE.getBytes());
        outputStream.flush();
    }

}
