package reactivestreams.proactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

@Slf4j
public class MessageCompletionHandler implements CompletionHandler<Integer, Void> {

    private final ByteBuffer requestByteBuffer;
    private final AsynchronousSocketChannel socketChannel;

    public MessageCompletionHandler(AsynchronousSocketChannel socketChannel) {
        this.requestByteBuffer = ByteBuffer.allocateDirect(1024);
        this.socketChannel = socketChannel;
        this.socketChannel.read(this.requestByteBuffer, null, this);
    }

    @Override
    public void completed(Integer result, Void attachment) {
        String requestBody = handleRequest();
        sendResponse(requestBody);
    }

    @Override
    public void failed(Throwable exc, Void attachment) {
        log.error("Failed to read from Client", exc);
    }

    private String handleRequest() {
        requestByteBuffer.flip();
        return StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
    }

    @SneakyThrows
    private void sendResponse(String requestBody) {
        // 응답까지에 지연요소
        Thread.sleep(100);
        log.info("sendResponse : {}", requestBody);
        ByteBuffer responseByteBuffer = ByteBuffer.wrap(requestBody.getBytes());
        socketChannel.write(responseByteBuffer, null, new CompletionHandler<>() {
            @SneakyThrows
            @Override
            public void completed(Integer result, Object attachment) {
                socketChannel.close();
            }

            @SneakyThrows
            @Override
            public void failed(Throwable exc, Object attachment) {
                socketChannel.close();
            }
        });
    }

}
