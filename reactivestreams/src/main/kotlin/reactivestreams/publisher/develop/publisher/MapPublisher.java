package reactivestreams.publisher.develop.publisher;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.function.Function;

/**
 * IterPublish를 구독하여 Subscriber OnNext로 내려오는 데이터를 중간에서 연산하여
 * MapPublisher를 구독하고있는 Subscriber OnNext를 호출하여 중간연산결과를 전달
 */
@Slf4j
public class MapPublisher implements Publisher {

    private Publisher publisher;
    private Function<Integer, Integer> function;

    public MapPublisher(Publisher publisher, Function<Integer, Integer> function) {
        this.publisher = publisher;
        this.function = function;
    }

    @Override
    public void subscribe(Subscriber studySubscriber) {
        publisher.subscribe(new MapSubscriber(studySubscriber, function));
    }

    static class MapSubscriber implements Subscriber<Integer> {

        private Subscriber subscriber; // StudySubscriber
        private Function<Integer, Integer> function;

        public MapSubscriber(Subscriber subscriber, Function<Integer, Integer> function) {
            this.subscriber = subscriber;
            this.function = function;
        }

        @Override
        public void onSubscribe(Subscription s) {
            // Subscription을 직접 생성하여 넘기고있으나 상위 Subscription을 넘겨도됨.
            // 만약 중간단계에서 배압처리등을 하려면 직접 생성한 Subscription을 넘기면될듯
            subscriber.onSubscribe(new MapSubscription(s));
        }

        @Override
        public void onNext(Integer integer) {
            subscriber.onNext(function.apply(integer));
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

    static class MapSubscription implements Subscription {

        private Subscription subscription; // IterSubscription

        public MapSubscription(Subscription subscription) {
            this.subscription = subscription;
        }

        @Override
        public void request(long n) {
            subscription.request(n);
        }

        @Override
        public void cancel() {
            subscription.cancel();
        }
    }

}
