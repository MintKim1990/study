package reactivestreams.publisher.develop.publisher;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Iterator;
import java.util.List;

@Slf4j
public class IterPublisher implements Publisher {

    private static final Iterable ITERABLE = List.of(1,2,3,4,5,6,7,8,9,10);

    @Override
    public void subscribe(Subscriber s) {
        s.onSubscribe(new IterSubscription(s));
    }

    static class IterSubscription implements Subscription {

        private Iterator iterator = ITERABLE.iterator();
        private Subscriber subscriber;

        public IterSubscription(Subscriber subscriber) {
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            // n개만큼 처리하려면 버퍼등 고려할 사항이 많아 개발용이므로 패스
            try {
                while (iterator.hasNext()) {
                    subscriber.onNext(iterator.next());
                }
                subscriber.onComplete();
            } catch (Exception error) {
                subscriber.onError(error);
            }
        }

        @Override
        public void cancel() {
            log.info("cancel");
        }
    }

}
