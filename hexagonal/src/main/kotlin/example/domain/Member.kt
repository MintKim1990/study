package example.domain

import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

class Member(
    var id: Long,
    var name: String,
    var email: String,
    var address: String,
    var valid: Boolean = true,
    var corp: Boolean,
) {



}