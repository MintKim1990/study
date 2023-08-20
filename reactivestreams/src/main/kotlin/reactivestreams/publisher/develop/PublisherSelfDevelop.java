package reactivestreams.publisher.develop;

import org.reactivestreams.Publisher;
import reactivestreams.publisher.develop.publisher.IterPublisher;
import reactivestreams.publisher.develop.publisher.FilterPublisher;
import reactivestreams.publisher.develop.publisher.MapPublisher;
import reactivestreams.publisher.develop.subscriber.StudySubscriber;

/**
 * 여러 동작을 하는 퍼블리셔를 공부목적으로 만들어보자.
 * 퍼블리셔를 체인형식으로 만들고 최상위 데이터 스트림 퍼블리셔에서 데이터를 내리면
 * 최상위 퍼블리셔를 구독하고있는 중간단계 Subscriber에서 데이터를 연산하고
 * 하위 Subscriber에게 내리는 형식식 */
public class PublisherSelfDevelop {
    public static void main(String[] args) {
        Publisher publisher = new IterPublisher();
        MapPublisher mapPublisher = new MapPublisher(publisher, integer -> integer * 10);
        FilterPublisher filterPublisher = new FilterPublisher(mapPublisher, integer -> integer < 90);

        filterPublisher.subscribe(new StudySubscriber());
    }
}
