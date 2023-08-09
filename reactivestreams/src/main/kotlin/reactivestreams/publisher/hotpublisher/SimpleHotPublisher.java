package reactivestreams.publisher.hotpublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Future;

/**
 * Hot Pushlisher는 지속적으로 데이터가 발생할때마다 구독한 Subscriber에게 publish 하는 방식을 의미한다.
 * 예를들어 SNS 등이며 지속적으로 데이터가 발생하는 Publisher에 Subscriber가 구독하면 구독한 시점부터에
 * 데이터를 지속적으로 제공받는다.
 */
public class SimpleHotPublisher implements Flow.Publisher<Integer> {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Future<Void> task;
    private final List<Integer> numbers = new ArrayList<>();
    private List<SimpleHotSubScription> subScriptions = new ArrayList<>();

    public SimpleHotPublisher() {
        numbers.add(1);
        this.task = makeDataThenAddNumbers();
    }

    // 지속적으로 numbers에 데이터를 삽입하는 비동기 코드
    private Future<Void> makeDataThenAddNumbers() {
        return executorService.submit(() -> {
            for (int i = 2; !Thread.interrupted(); i++) {
                numbers.add(i);
                // 아래에 onNextWhilePossible 메서드를 데이터를 생성하는 속도보다 Subscriber가
                // 빨리 읽게 될경우 대기상태에 빠지게되는데 이를 위해 데이터가 생성될때마다
                // 구독한 Subscriber들에게 지속적으로 푸쉬하는 로직
                // 해당 Publisher는 지속적으로 데이터를 생성하다보니 Subscriber에게 OnComplete()
                // 메서드를 호출하지 않고 지속적으로 푸쉬하는 Publisher
                subScriptions.forEach(SimpleHotSubScription::wakeup);
                Thread.sleep(100);
            }
            return null;
        });
    }

    public void shutdown() {
        this.task.cancel(true);
        executorService.shutdown();
    }

    @Override
    public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
        var subscription = new SimpleHotSubScription(subscriber);
        subscriber.onSubscribe(subscription);
        subScriptions.add(subscription); // 데이터 푸쉬를 위해 Subscription을 Publisher에 저장
    }

    private class SimpleHotSubScription implements Flow.Subscription {

        private int offset; // Subscriber가 데잍터를 읽을 인덱스 크기
        private int requiredOffset; // Subscriber가 데이터를 읽을 최대 인덱스 크기
        private final Flow.Subscriber<? super Integer> subscriber;
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();

        public SimpleHotSubScription(Flow.Subscriber<? super Integer> subscriber) {
            int lastElementIndex = numbers.size() - 1;
            this.offset = lastElementIndex;
            this.requiredOffset = lastElementIndex;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            requiredOffset += n;
            onNextWhilePossible();
        }

        // Publisher에서 데이터가 발생하면 Subscriber들에게 지속적으로 데이터 푸쉬
        public void wakeup() {
            onNextWhilePossible();
        }

        @Override
        public void cancel() {
            this.subscriber.onComplete();
            if (subScriptions.contains(this)) {
                subScriptions.remove(this);
            }
            executorService.shutdown();
        }

        // Subscriber에 onNext 메서드로 데이터 지속적으로 푸쉬
        // Subscription.request 크기를 크게잡으면 데이터 생성되는 속도를 Subscriber가 따라잡아
        // 대기에 빠질수있으므로 Publisher 데이터 생성 로직에서 지속적으로 Subscriber들에게 푸쉬
        // 지속적으로 생성되는 로직이라 Subscriber.onComplete() 메서드를 호출하지 않는다.
        private void onNextWhilePossible() {
            executorService.submit(() -> {
                while (offset < requiredOffset && offset < numbers.size()) {
                    var item = numbers.get(offset);
                    subscriber.onNext(item);
                    offset++;
                }
            });
        }

    }
}
