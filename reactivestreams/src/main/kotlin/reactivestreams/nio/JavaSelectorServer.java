package reactivestreams.nio;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 리눅스 기반 OS에서는 파일, 디렉토리, 소켓, 파이프, 어플리케이션 객체 등등
 * 모든 객체를 파일로 관리하며 이에 접근하기 위해 FileDescriptor에 해당 객체들에 정보를 저장한다.
 * FileDescriptor에는 fd_set이라는 값이 존재하는데 해당 값으로 객체에 변경유무등에 대한 정보를 기재한다.
 *
 * 또한 FileDescriptor 객체들에 변동여부를 알기위해 epoll을 지원하는데
 * epoll은 FileDescriptor의 fd_set를 관찰하고 I/O 준비가 된 FileDescriptor 객체가 있다면
 * 어플리케이션에게 전달한다.
 *
 * epoll 함수
 * epoll_create : epoll 인스턴스를 생성
 * epoll_ctl : epoll 인스턴스에 fd와 관심있는 이벤트를 등록/삭제/수정
 * epoll_wait : fd와 관련된 이벤트를 관찰
 *
 * Selector
 * OS 레벨에 자료구조를 직접 다룰수 없으니 이를 포팅한 Selector를 자바에서 지원
 * 아래 코드는 Selector 기반으로한 소켓 프로그래밍이며 epoll 함수를 어느시점에 호출하는지 주석으로 작성
 *
*/
@Slf4j
public class JavaSelectorServer {

    private static ExecutorService executorService = Executors.newFixedThreadPool(50);

    @SneakyThrows
    public static void main(String[] args) {
        try (
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                // 해당 코드를 따라들어가보면 WEPollSelectorImpl 내부에서 WEPoll.create() 메서드를 호출하는데 해당 부분이 epoll_create 를 할것으로 예상
                Selector selector = Selector.open()
        ) {
            serverSocketChannel.bind(new InetSocketAddress("localhost", 8080));
            // 해당설정을 false로 변경시 nonBlocking 방식으로 동작 기본은 true (Blocking)
            serverSocketChannel.configureBlocking(false);
            // 내부적으로 따라들어가면 epoll_ctl 메서드를 호출하여 관심있는 이벤트를 등록
            // 아래 코드에서는 serverSocketChannel도 FileDescriptor에 저장되어있을거고 해당 채널에 클라이언트가 커넥션 Accept 이벤트 등록
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while(true) {
                // Selector에서 이벤트가 발생할때까지 Blocking 처리되며 이는 내부적으로 epoll_wait 메서드를 타임아웃을 무한으로 지정하여 호출
                // epoll에 등록된 FileDescriptor중 이벤트가 발생할경우 대기상태에서 빠져나옴
                selector.select();
                // Selector.selectedKeys() 조회시 발생한 이벤트에 대한 정보를 리턴
                Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();

                while (selectedKeys.hasNext()) {
                    SelectionKey key = selectedKeys.next();
                    selectedKeys.remove();

                    // 클라이언트로부터 연결요청이 온경우
                    if (key.isAcceptable()) {
                        SocketChannel clientSocket = ((ServerSocketChannel) key.channel()).accept();
                        clientSocket.configureBlocking(false);
                        // 내부적으로 따라들어가면 epoll_ctl 메서드를 호출하여 관심있는 이벤트를 등록
                        // 아래 코드에서는 clientSocket도 FileDescriptor에 저장되어있을거고 해당 채널에 read 이벤트를 등록
                        clientSocket.register(selector, SelectionKey.OP_READ);
                    }

                    // 읽기 이벤트가 발생한 경우
                    else if (key.isReadable()) {
                        SocketChannel clientSocket = ((SocketChannel) key.channel());
                        String requestBody = handleRequest(clientSocket);
                        sendResponse(clientSocket, requestBody);
                    }


                }
            }
        }
    }

    @SneakyThrows
    private static String handleRequest(SocketChannel channel) {
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

        String requestBody = StandardCharsets.UTF_8.decode(requestByteBuffer).toString();
        log.info("request Body : {}", requestBody);
        return requestBody;
    }

    @SneakyThrows
    private static void sendResponse(SocketChannel channel, String requestBody) {
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
            try {
                // 응답까지에 지연요소
                Thread.sleep(100);

                String content = "received : " + requestBody;
                ByteBuffer responseByteBuffer = ByteBuffer.wrap(content.getBytes());
                channel.write(responseByteBuffer);
                channel.close();

            } catch (Exception error) {}
        }, executorService);
    }
}
