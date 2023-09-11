package reactivestreams.coroutine.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("USER")
class User(
    @Id
    var id: Long?,
    val name: String,
    val age: Int,
    val password: String,
) {
    @CreatedDate
    var createdAt: LocalDateTime? = null

    @LastModifiedDate
    var updatedAt: LocalDateTime? = null

    constructor(name: String, age: Int, password: String) : this(null, name, age, password)

}