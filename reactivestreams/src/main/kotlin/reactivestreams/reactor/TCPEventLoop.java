package reactivestreams.reactor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
 * Reactor
 * 리액터패턴은 이벤트를 관찰하고 담당하는 큐 역활을 수행하는 Selector에서 발생한 이벤트를
 * 동시에 받아 처리하는 이벤트 핸들링 패턴으로 이벤트를 처리하는 핸들러를 구현하여 처리하는 방식을 의미
 * 아래 코드에서는 Accept를 처리하는 Acceptor, Read를 처리하는 MessageEventHandler가 핸들러
 *
*/
@Slf4j
public class TCPEventLoop implements EventLoop {

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final EventHandler acceptor;

    @SneakyThrows
    public TCPEventLoop(int port) {
        selector = Selector.open();
        // Selector에서 발생하는 클라이언트 접속 요청 Accept 이벤트를 처리하는 핸들러
        acceptor = new Acceptor(new MessageEventHandler(), selector);

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("localhost", port));
        // 해당설정을 false로 변경시 nonBlocking 방식으로 동작 기본은 true (Blocking)
        serverSocketChannel.configureBlocking(false);
        // 내부적으로 따라들어가면 epoll_ctl 메서드를 호출하여 관심있는 이벤트를 등록
        // 아래 코드에서는 serverSocketChannel도 FileDescriptor에 저장되어있을거고
        // 해당 채널에 클라이언트가 커넥션 Accept 이벤트 등록
        // attach 메서드는 Accept 이벤트가 발생했을때 처리할 처리자를 SelectionKey에 저장하는 메서드
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT).attach(acceptor);
    }

    @Override
    public void run() {
        executorService.submit(() -> {
           while (true) {
               // Selector에서 이벤트가 발생할때까지 Blocking 처리되며 이는 내부적으로 epoll_wait 메서드를 타임아웃을 무한으로 지정하여 호출
               // epoll에 등록된 FileDescriptor중 이벤트가 발생할경우 대기상태에서 빠져나옴
               selector.select();
               // Selector.selectedKeys() 조회시 발생한 이벤트에 대한 정보를 리턴
               Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

               while (selectionKeys.hasNext()) {
                   SelectionKey key = selectionKeys.next();
                   selectionKeys.remove();

                   dispatch(key);
               }
           }
        });
    }

    private void dispatch(SelectionKey selectionKey) {
        // 발생한 이벤트에 대한 정보를 담고있는 SelectionKey에서 핸들러 객체를 꺼냄
        EventHandler eventHandler = (EventHandler) selectionKey.attachment();

        if (selectionKey.isReadable() || selectionKey.isAcceptable()) {
            // 핸들러에게 SelectionKey 정보를 넘겨서 핸들링 처리 요청
            eventHandler.handle(selectionKey);
        }
    }

}
