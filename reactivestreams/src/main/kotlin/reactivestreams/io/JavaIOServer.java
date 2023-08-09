package reactivestreams.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class JavaIOServer {

    private static final String MESSAGE = "This is Server";

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket()) {
            serverSocket.bind(new InetSocketAddress("localhost", 8080));

            while(true) {
                Socket clientSocket = serverSocket.accept(); // 블락킹으로 동작

                byte[] requestBytes = new byte[1024];
                InputStream inputStream = clientSocket.getInputStream();
                inputStream.read(requestBytes);
                log.info("request Body : {}", new String(requestBytes).trim());

                OutputStream outputStream = clientSocket.getOutputStream();
                outputStream.write(MESSAGE.getBytes());
                outputStream.flush();
            }
        }
    }
}
