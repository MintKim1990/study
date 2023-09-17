package example.adapter.out.persistence

import example.adapter.PersistenceAdapter
import example.application.port.out.MemberCreatePort
import example.application.port.out.MemberFindPort
import org.springframework.data.repository.findByIdOrNull

@PersistenceAdapter
class MemberPersistenceAdapter(
    private val memberRepository: MemberRepository,
) : MemberCreatePort, MemberFindPort {

    override fun createMember(name: String, address: String, email: String, isCorp: Boolean): MemberEntity {

        val memberEntity = MemberEntity(
            name = name,
            email = email,
            address = address,
            corp = isCorp
        )

        return memberRepository.save(memberEntity)
    }

    override fun findMember(id: Long): MemberEntity {
        return memberRepository.findByIdOrNull(id) ?: throw NoSuchElementException()
    }

}