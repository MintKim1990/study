package reactivestreams.io;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class JavaIOClient {

    private static final String MESSAGE = "This is Client";

    @SneakyThrows
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", 8080));

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(MESSAGE.getBytes());
            outputStream.flush();

            InputStream inputStream = socket.getInputStream();
            byte[] responseBytes = new byte[1024];
            inputStream.read(responseBytes);
            log.info("response Body : {}", new String(responseBytes).trim());
        }
    }
}
