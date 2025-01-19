package reactivestreams.r2dbcspi;

import lombok.ToString;

@ToString
public class Person {

    private Long id;
    private String name;

    public Person(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
