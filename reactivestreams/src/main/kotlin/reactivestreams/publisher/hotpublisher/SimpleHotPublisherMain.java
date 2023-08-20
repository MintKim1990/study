package reactivestreams.publisher.hotpublisher;

public class SimpleHotPublisherMain {

    public static void main(String[] args) throws InterruptedException {

        var publisher = new SimpleHotPublisher();

        // 데이터가 생성될때마다 지속적으로 데이터를 받는부분 확인
        var subscriber = new SimpleNamedSubscriber<>("subscriber1");
        publisher.subscribe(subscriber);

        Thread.sleep(5000);
        subscriber.cancle();

        // 두명에 구독자가 구독시 두명이 지속적으로 데이터를 받는 부분 확인
        var subscriber2 = new SimpleNamedSubscriber<>("subscriber2");
        var subscriber3 = new SimpleNamedSubscriber<>("subscriber3");
        publisher.subscribe(subscriber2);
        publisher.subscribe(subscriber3);

        Thread.sleep(5000);
        subscriber2.cancle();
        subscriber3.cancle();

        Thread.sleep(1000);

        // 위에 1초 대기 후 구독자 추가 시 구독시점부터 데이터를 받는 부분 확인
        var subscriber4 = new SimpleNamedSubscriber<>("subscriber4");
        publisher.subscribe(subscriber4);

        Thread.sleep(5000);
        subscriber4.cancle();

        publisher.shutdown();
    }

}
