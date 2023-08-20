package reactivestreams.publisher.develop.publisher;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Predicate;

public class FilterPublisher implements Publisher {

    private Publisher publisher;
    private Predicate<Integer> predicate;

    public FilterPublisher(Publisher publisher, Predicate<Integer> predicate) {
        this.publisher = publisher;
        this.predicate = predicate;
    }

    @Override
    public void subscribe(Subscriber s) {
        publisher.subscribe(new FilterSubscriber(s, predicate));
    }

    static class FilterSubscriber implements Subscriber<Integer> {

        private Subscriber subscriber;
        private Predicate<Integer> predicate;

        public FilterSubscriber(Subscriber subscriber, Predicate<Integer> predicate) {
            this.subscriber = subscriber;
            this.predicate = predicate;
        }

        // 상위 Publisher에서 전달하는 Subscription을 하위 Subscriber에게 그대로 전달하면
        // 하위 Subscriber는 상위 Publisher에서 전달한 Subscription에 request 메서드를 호출
        // 해당 소스에서는 말그대로 전달하는 역활만 수행
        @Override
        public void onSubscribe(Subscription s) {
            subscriber.onSubscribe(s);
        }

        @Override
        public void onNext(Integer integer) {
            if (predicate.test(integer)) {
                subscriber.onNext(integer);
            }
        }

        @Override
        public void onError(Throwable t) {
            subscriber.onError(t);
        }

        @Override
        public void onComplete() {
            subscriber.onComplete();
        }
    }

}
