package reactivestreams.publisher.coldpublisher;

import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Cold Publisher는 Subscriber가 구독할때마다 새로운 데이터를 생성하여 publish 하는 방식을 의미한다.
 * 예를들어 API, 파일읽기등 Subscriber가 100개가 되더라도 각 Subscriber에 제공하는 데이터는 동일한 경우를 의미
 */
public class SimpleColdPublisher implements Flow.Publisher<Integer> {
    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        // 이처럼 Subscriber들이 구독할때마다 같은 데이터를 생성하여 내려주는 방식이 ColdPublisher
        var iterator = Collections.synchronizedList(
                IntStream.range(1, 10).boxed().collect(Collectors.toList())
        ).iterator();

        var subsciption = new SimpleColdSubsciption(iterator, subscriber);
        subscriber.onSubscribe(subsciption);
    }

    public class SimpleColdSubsciption implements Flow.Subscription {

        private final Iterator<Integer> iterator;
        private final Flow.Subscriber<? super Integer> subscriber;
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();

        public SimpleColdSubsciption(Iterator<Integer> iterator, Flow.Subscriber<? super Integer> subscriber) {
            this.iterator = iterator;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            executorService.submit(() -> {
                for (int i = 0; i < n; i++) {
                    if (iterator.hasNext()) {
                        var number = iterator.next();
                        System.out.println(String.format("SimpleColdSubsciption thread : {%s}, request value : {%s}, onNext value : {%s}",
                                Thread.currentThread().getName(), n, number));
                        iterator.remove();
                        subscriber.onNext(number);
                    } else {
                        subscriber.onComplete();
                        executorService.shutdown();
                        break;
                    }
                }
            });
        }

        @Override
        public void cancel() {
            subscriber.onComplete();
        }
    }

}
