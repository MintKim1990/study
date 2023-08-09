package reactivestreams.publisher.coldpublisher;

import reactivestreams.publisher.subscriber.SimpleNamedSubscriber;

public class SimpleColdPublisherMain {

    public static void main(String[] args) throws InterruptedException {
        var publisher = new SimpleColdPublisher();
        var subscriber = new SimpleNamedSubscriber<Integer>("subscriber1");
        publisher.subscribe(subscriber);

        Thread.sleep(1000);

        var subscriber2 = new SimpleNamedSubscriber<Integer>("subscriber1");
        publisher.subscribe(subscriber2);
    }

}
