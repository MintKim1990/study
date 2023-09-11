package reactivestreams.coroutine.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("AUTH")
class Auth(
    @Id
    var id: Long?,
    val userId: Long,
    val token: String,
) {

    @CreatedDate
    private var createdAt: LocalDateTime? = null

    @LastModifiedDate
    private var updatedAt: LocalDateTime? = null

    constructor(userId: Long, token: String) : this(null, userId, token)

}