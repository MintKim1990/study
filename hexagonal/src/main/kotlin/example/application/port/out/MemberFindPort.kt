package example.application.port.out

import example.adapter.out.persistence.MemberEntity

interface MemberFindPort {
    fun findMember(id: Long): MemberEntity
}