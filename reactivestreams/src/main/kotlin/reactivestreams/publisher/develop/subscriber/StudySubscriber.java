package reactivestreams.publisher.develop.subscriber;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

@Slf4j
public class StudySubscriber implements Subscriber {

    @Override
    public void onSubscribe(Subscription s) {
        log.info("onSubscribe");
        s.request(Long.MAX_VALUE);
    }

    @Override
    public void onNext(Object o) {
        log.info("onNext : {}", o);
    }

    @Override
    public void onError(Throwable t) {
        log.error("onError : {}", t);
    }

    @Override
    public void onComplete() {
        log.info("onComplete");
    }
}
