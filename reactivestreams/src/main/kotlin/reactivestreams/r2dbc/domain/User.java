package reactivestreams.r2dbc.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Table("USER")
@Data
public class User {

    @Id
    private Long id;
    private String name;
    private Integer age;
    private String password;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public User(String name, Integer age, String password) {
        this.name = name;
        this.age = age;
        this.password = password;
    }

}
