package reactivestreams.r2dbcspi;

import io.asyncer.r2dbc.mysql.MySqlConnectionConfiguration;
import io.asyncer.r2dbc.mysql.MySqlConnectionFactory;
import io.asyncer.r2dbc.mysql.MySqlResult;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
public class R2DBCSpiApplication {

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE IF NOT EXISTS person
            (id INT AUTO_INCREMENT PRIMARY KEY,
            name VARCHAR(255)
            )
            """;

    private static final String INSERT_SQL = "INSERT INTO person(name) VALUES (?name)";
    private static final String SELECT_SQL = "SELECT * FROM person";

    public static void main(String[] args) {

        MySqlConnectionConfiguration mySqlConnectionConfiguration = MySqlConnectionConfiguration.builder()
                .host("localhost")
                .port(3306)
                .username("root")
                .password("1234")
                .database("study")
                .build();


        MySqlConnectionFactory mySqlConnectionFactory = MySqlConnectionFactory.from(mySqlConnectionConfiguration);
        mySqlConnectionFactory.create()
                .flatMapMany(mySqlConnection -> {
//                    Flux<MySqlResult> create = mySqlConnection.createStatement(CREATE_TABLE_SQL).execute();
//                    Flux<MySqlResult> insert = mySqlConnection.createStatement(INSERT_SQL)
//                            .bind("name", "mint")
//                            .execute();

                    Flux<MySqlResult> select = mySqlConnection.createStatement(SELECT_SQL).execute();

//                    Flux<MySqlResult> result = create.thenMany(insert).thenMany(select);
//                    return result;
                    return select;
                })
                .flatMap(mySqlResult -> mySqlResult.map((row, rowMetadata) -> {
                   Long id = row.get("id", Long.class);
                   String name = row.get("name", String.class);
                   return new Person(id, name);
                }))
                .subscribe(person -> {
                    log.info("person: {}", person);
                });

        // Main 스레드가 바로 종료되면 비동기 로직이 동작하지 않으므로 Sleep
        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
