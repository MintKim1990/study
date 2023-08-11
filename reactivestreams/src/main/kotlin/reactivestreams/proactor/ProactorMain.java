package reactivestreams.proactor;

/**
 * Java NIO 기반에 Reactor 패턴은 커널 <-> Selector <-> application 구조에서
 * Java AIO 기반에 Proactor 패턴은 커널 <-> application 구조로
 * Selector에서 병목이 발생한다면 시스템 전체가 지연되는 부분을 고려하여
 * 커널에 다이렉트로 접근하여 처리함으로 병목이 제거되어 더 빠른 처리가 지원되며
 * 이벤트별로 콜백을 등록하여 이벤트 발생시 콜백 핸들러를 통하여 이벤트를 처리한다.
 *
 * Reactor 패턴에서 조금 더 발전된 패턴이지만 Netty에서는 Reactor 패턴만으로도 충분한 성능을 낼수있는 부분과
 * 커널에 다이렉트로 접근하는 부분에 대한 고려로 Reactor 기반으로 구현되었다.
 *
 * 따라서 Proactor 패턴은 참고용으로만 알고있자.
 */
public class ProactorMain {
    public static void main(String[] args) throws InterruptedException {
        Proactor proactor = new Proactor(8080);
        proactor.run();

        // Proactor는 Reactor 패턴에 Selector 처럼 커널 이벤트가 발생할때까지 Blocking 하는 로직을 지원하지 않고
        // 비동기로 동작하기때문에 Main 스레드를 무한정 대기
       Thread.sleep(Long.MAX_VALUE);
    }
}
