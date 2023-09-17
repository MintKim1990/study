package example.adapter.out.persistence

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "member")
class MemberEntity(

    @Id
    @GeneratedValue
    var id: Long? = null,
    var name: String,
    var email: String,
    var address: String,
    var valid: Boolean = true,
    var corp: Boolean,

) {



}