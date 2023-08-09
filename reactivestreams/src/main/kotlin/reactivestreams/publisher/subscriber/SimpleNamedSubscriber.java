package reactivestreams.publisher.subscriber;

import java.util.concurrent.Flow;

public class SimpleNamedSubscriber<T> implements Flow.Subscriber<T> {

    private final String name;
    private Flow.Subscription subscription;

    public SimpleNamedSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
        System.out.println(String.format("SimpleNamedSubscriber thread : {%s}, name : {%s}, onSubscribe", Thread.currentThread().getName(), name));
    }

    @Override
    public void onNext(T item) {
        System.out.println(String.format("SimpleNamedSubscriber thread : {%s}, name : {%s}, onNext : {%s}", Thread.currentThread().getName(), name, item));
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(String.format("SimpleNamedSubscriber thread : {%s}, name : {%s}, onError : {%s}", Thread.currentThread().getName(), name, throwable.getMessage()));
    }

    @Override
    public void onComplete() {
        System.out.println(String.format("SimpleNamedSubscriber thread : {%s}, name : {%s}, onComplete", Thread.currentThread().getName(), name));
    }

    public void cancle() {
        System.out.println(String.format("SimpleNamedSubscriber thread : {%s}, name : {%s}, cancel", Thread.currentThread().getName(), name));
        this.subscription.cancel();
    }
}
