package example.application.port.out

import example.adapter.out.persistence.MemberEntity

interface MemberCreatePort {
    fun createMember(name: String, address: String, email: String, isCorp: Boolean): MemberEntity
}