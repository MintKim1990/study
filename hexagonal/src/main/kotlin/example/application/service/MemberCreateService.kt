package example.application.service

import example.application.UseCase
import example.application.port.`in`.MemberCreateUseCase
import example.application.port.`in`.command.MemberCreateCommand
import example.application.port.out.MemberCreatePort
import example.domain.Member
import org.springframework.transaction.annotation.Transactional
import javax.validation.Validation

@Transactional(readOnly = true)
@UseCase
class MemberCreateService(
    private val memberCreatePort: MemberCreatePort,
    private val memberMapper: MemberMapper,
) : MemberCreateUseCase {

    @Transactional
    override fun memberCreate(memberCreateCommand: MemberCreateCommand): Member {

        val memberEntity = memberCreatePort.createMember(
            name = memberCreateCommand.name,
            address = memberCreateCommand.address,
            email = memberCreateCommand.email,
            isCorp = memberCreateCommand.isCorp
        )

        return memberMapper.toMember(memberEntity)
    }

}